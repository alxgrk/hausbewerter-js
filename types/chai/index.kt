@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS", "EXTERNAL_DELEGATION", "NESTED_CLASS_IN_EXTERNAL_INTERFACE")

package chai

@JsModule("chai")
external val chai: ChaiStatic = definedExternally
external var should: Assertion
    get() = definedExternally
    set(value) = definedExternally