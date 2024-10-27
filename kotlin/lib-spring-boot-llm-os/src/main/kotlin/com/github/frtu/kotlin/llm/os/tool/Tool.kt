package com.github.frtu.kotlin.llm.os.tool

import com.github.frtu.kotlin.action.execution.GenericAction

/**
 * Tool is an abstraction for simple function and a complex agent containing
 *
 * - All the action metadata
 * - An execution function to call
 *
 * @author Frédéric TU
 * @since 2.0.6
 */
interface Tool : GenericAction {
    /** Name of the tool */
    val name: String

    /** Description that can be used by agent to decide which tool to use */
    val description: String

    /** Input parameter schema (recommend to only have one parameter) */
    val parameterJsonSchema: String

    /** Return schema. `null` schema when returning `void` */
    val returnJsonSchema: String?
        get() = null
}