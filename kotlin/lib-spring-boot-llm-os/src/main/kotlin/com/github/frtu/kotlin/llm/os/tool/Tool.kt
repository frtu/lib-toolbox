package com.github.frtu.kotlin.llm.os.tool

import com.fasterxml.jackson.databind.JsonNode

/**
 * Tool is an abstraction for simple function and a complex agent.
 *
 * @author Frédéric TU
 * @since 2.0.6
 */
interface Tool : Executable {
    /** Name of the tool */
    val name: String

    /** Description that can be used by agent to decide which tool to use */
    val description: String

    /** Input parameter schema (recommend to only have one parameter) */
    val parameterJsonSchema: String

    /** Return schema. `null` schema when returning `void` */
    val returnJsonSchema: String?
        get() = null

    companion object {
        fun create(
            name: String,
            description: String,
            parameterJsonSchema: String,
            returnJsonSchema: String? = null,
            executer: (JsonNode) -> JsonNode
        ): Tool = object : Tool {
            override val name = name
            override val description = description
            override val parameterJsonSchema = parameterJsonSchema
            override val returnJsonSchema = returnJsonSchema
            override suspend fun execute(parameter: JsonNode): JsonNode = executer.invoke(parameter)
        }
    }
}
