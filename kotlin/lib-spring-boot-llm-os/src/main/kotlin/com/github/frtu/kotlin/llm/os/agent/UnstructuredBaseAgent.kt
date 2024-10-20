package com.github.frtu.kotlin.llm.os.agent

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.TextNode
import com.github.frtu.kotlin.llm.os.llm.Chat
import com.github.frtu.kotlin.llm.os.tool.function.FunctionRegistry
import com.github.frtu.kotlin.serdes.json.schema.SchemaGen.STRING_SCHEMA

/**
 * An agent receiving & returning text
 *
 * @author Frédéric TU
 * @since 2.0.6
 */
class UnstructuredBaseAgent(
    /** Name */
    name: String,
    /** Description */
    description: String,
    /** System instruction prompt */
    instructions: String,
    /** Engine containing model version */
    chat: Chat,
    /** For function / tool execution */
    toolRegistry: FunctionRegistry? = null,
    isStateful: Boolean = false,
) : AgentExecutor(
    name = name,
    description = description,
    instructions = instructions,
    chat = chat,
    toolRegistry = toolRegistry,
    parameterJsonSchema = STRING_SCHEMA,
    returnJsonSchema = STRING_SCHEMA,
    isStateful = isStateful,
) {
    override suspend fun execute(parameter: JsonNode): JsonNode {
        val request = parameter.asText()
        val sendMessage = answer(request)
        return TextNode(sendMessage.content)
    }

    suspend fun execute(request: String): String = answer(request).content!!
}