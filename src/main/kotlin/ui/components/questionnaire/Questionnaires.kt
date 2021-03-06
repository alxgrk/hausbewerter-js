package ui.components.questionnaire

import di.questionRepo
import kotlinext.js.Object
import kotlinext.js.js
import libraries.react.material.data.CardData
import libraries.react.material.data.CardSize
import libraries.react.router.Link
import libraries.react.router.redirect
import react.*
import react.dom.*
import ui.components.cards.cardRow
import ui.components.various.iconButton
import various.*
import kotlin.js.Json

interface QuestionnairesState : RState {
    var redirectTo: String
    var questionnaires: List<Json>
}

class Questionnaires : RComponent<RProps, QuestionnairesState>() {

    init {
        state.questionnaires = emptyList()
    }

    override fun componentWillMount() {
        questionRepo.getAll { response ->
            setState {
                val members = response.data.toJson().asList("members")
                questionnaires = members
            }
        }
    }

    override fun RBuilder.render() {
        div(classes = "questionnaire") {
            h1("questionnaires-all") {
                +allQuestionnaires()
            }

            // last card for adding a questionnaire
            val addingCard = CardData(title = addNewQuestionnaire(),
                    size = CardSize.SMALL) {
                iconButton(iconText = "add", className = "center medium") {
                    questionRepo.create { response ->
                        val newId = response.data.toJson()["id"].toString()
                        console.log("created new questionnaire with id $newId")
                        setState {
                            redirectTo = "/questionnaire/$newId"
                        }
                        return@create response
                    }
                }
            }

            if (state.questionnaires.isEmpty()) {
                cardRow(l = 6, m = 6, s = 12, cards = listOf(addingCard))
            }

            state.questionnaires.forEachIndexed { index, member ->

                // every second time and only if there is a next card
                if (index % 2 == 0) {

                    val secondCard =
                            if ((index + 1) < state.questionnaires.size)
                                memberToCard(state.questionnaires[index + 1])
                            else
                                addingCard

                    val members = listOf(memberToCard(member), secondCard)
                    cardRow(l = 6, m = 6, s = 12, cards = members)

                } else if ((index + 1) == state.questionnaires.size)
                    cardRow(l = 6, m = 6, s = 12, cards = listOf(addingCard))
            }

        }

        if (state.redirectTo !== undefined && state.redirectTo !== "") {
            console.log("redirecting to ${state.redirectTo}")
            val to = state.redirectTo
            setState {
                redirectTo = ""
            }
            redirect(to)
        }
    }

    private fun memberToCard(member: Json): CardData {
        val action = member["id"].toString().let { id ->
            console.log("showing details of questionnaire with id $id")
            createElement(
                    Link::class.js,
                    js { to = "/questionnaire/$id" }.unsafeCast<RProps>(),
                    edit()
            )
        }

        return CardData(
                title = member["name"].toString(),
                size = CardSize.SMALL,
                actions = arrayOf(action)) {

            if (member["state"] !== undefined) {
                p("questionnaire-overview-card card-panel") {
                    +"Status: "
                    toStateBagde(member["state"].toString())
                }
            }
            if (member["gesamtwert"] !== undefined) {
                p("questionnaire-overview-card card-panel") {
                    +"Gesamtwert: "
                    toStateBagde(member["gesamtwert"].toString())
                }
            }

        }
    }

    private fun RBuilder.toStateBagde(property: String?) =
            when (property) {
                "OPEN" -> span("status-badge badge blue lighten-2 white-text") { +stateOpen() }
                "FINISHED" -> span("status-badge badge green lighten-2 white-text") { +stateFinished() }
                else -> span("status-badge badge grey lighten-1 white-text") { +stateUnknown() }
            }
}
