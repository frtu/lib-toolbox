package com.github.frtu.kotlin.llm.os.tool

/**
 * ToolExecuter the default implementation and constructor for Tool
 *
 * @author Frédéric TU
 * @since 2.0.7
 */
abstract class ToolExecuter(
    /** Name of the tool */
    override val name: String,
    /** Description that can be used by agent to decide which tool to use */
    override val description: String,
    /** Input parameter schema (recommend to only have one parameter) */
    override val parameterJsonSchema: String,
    /** Return schema. `null` schema when returning `void` */
    override val returnJsonSchema: String? = null,
) : Tool