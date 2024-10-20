package com.github.frtu.kotlin.llm.os.tool

/**
 * Tool is an abstraction for simple function and a complex agent.
 *
 * @author Frédéric TU
 * @since 2.0.6
 */
abstract class Tool(
    /** Name of the tool */
    val name: String,
    /** Description that can be used by agent to decide which tool to use */
    val description: String,
    /** Input parameter schema (recommend to only have one parameter) */
    val parameterJsonSchema: String,
    /** Return schema. `null` schema when returning `void` */
    val returnJsonSchema: String? = null,
) : Executable