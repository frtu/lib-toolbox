package com.github.frtu.kotlin.llm.os.tool

import com.fasterxml.jackson.databind.JsonNode
import com.github.frtu.kotlin.action.management.ActionId

/**
 * ToolExecuter the default implementation and constructor for Tool
 *
 * @author Frédéric TU
 * @since 2.0.7
 */
abstract class ToolExecuter(
    /** Id of the tool */
    override val id: ActionId,
    /** Description that can be used by agent to decide which tool to use */
    override val description: String,
    /** Input parameter schema (recommend to only have one parameter) */
    override val parameterJsonSchema: String,
    /** Return schema. `null` schema when returning `void` */
    override val returnJsonSchema: String? = null,
) : Tool {
    companion object {
        fun create(
            id: String,
            description: String,
            parameterJsonSchema: String,
            returnJsonSchema: String? = null,
            executer: (JsonNode) -> JsonNode,
        ): Tool = object : Tool {
            override val id = ActionId(id)
            override val description = description
            override val parameterJsonSchema = parameterJsonSchema
            override val returnJsonSchema = returnJsonSchema
            override suspend fun execute(parameter: JsonNode): JsonNode = executer.invoke(parameter)
        }
    }
}