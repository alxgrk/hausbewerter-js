package ui.components.login

import libraries.react.material.data.InputData
import libraries.react.material.input
import org.w3c.dom.events.Event
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import various.placeholderUsername

interface UsernameProps : RProps {
    var onUsernameChanged: (Event) -> Unit
}

class UsernameInput(props: UsernameProps) : RComponent<UsernameProps, RState>(props) {

    override fun RBuilder.render() {
        input(
                InputData(label = placeholderUsername(),
                        icon = "account_circle",
                        onChange = props.onUsernameChanged)
        )
    }
}

fun RBuilder.usernameInput(onUsernameChanged: (Event) -> Unit) = child(UsernameInput::class) {
    attrs.onUsernameChanged = onUsernameChanged
}
