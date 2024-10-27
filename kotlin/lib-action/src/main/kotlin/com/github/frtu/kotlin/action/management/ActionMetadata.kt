package com.github.frtu.kotlin.action.management

/**
 * ActionMetadata is a description for an action.
 *
 * @author Frédéric TU
 * @since 2.0.7
 */
interface ActionMetadata {
    /** Id of the tool */
    val id: ActionId

    /** Description that can be used to decide which action to use */
    val description: String

    /** Input parameter schema (recommend to only have one parameter) */
    val parameterJsonSchema: String

    /** Return schema. `null` schema when returning `void` */
    val returnJsonSchema: String?
        get() = null
}
