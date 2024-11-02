package com.github.frtu.kotlin.ai.os.llm.agent

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.TextNode
import com.github.frtu.kotlin.action.management.ActionId
import com.github.frtu.kotlin.action.tool.ToolRegistry
import com.github.frtu.kotlin.ai.os.llm.Chat
import com.github.frtu.kotlin.serdes.json.schema.SchemaGen.STRING_SCHEMA

/**
 * An agent receiving & returning text
 *
 * @author Frédéric TU
 * @since 2.0.6
 */
open class UnstructuredBaseAgent(
    /** Id of the agent */
    id: ActionId,
    /** Description */
    description: String,
    /** System instruction prompt */
    instructions: String,
    /** Engine containing model version */
    chat: Chat,
    /** For function / tool execution */
    toolRegistry: ToolRegistry? = null,
    isStateful: Boolean = false,
) : AgentExecuter(
    id = id,
    description = description,
    instructions = instructions,
    chat = chat,
    toolRegistry = toolRegistry,
    parameterJsonSchema = STRING_SCHEMA,
    returnJsonSchema = STRING_SCHEMA,
    isStateful = isStateful,
) {
    constructor(
        id: String,
        description: String,
        instructions: String,
        chat: Chat,
        toolRegistry: ToolRegistry? = null,
        isStateful: Boolean = false,
    ) : this(
        id = ActionId(id),
        description = description,
        instructions = instructions,
        chat = chat,
        toolRegistry = toolRegistry,
        isStateful = isStateful,
    )

    override suspend fun execute(parameter: JsonNode): JsonNode {
        val request = parameter.asText()
        val sendMessage = answer(request)
        return TextNode(sendMessage.content)
    }

    open suspend fun execute(request: String): String = answer(request).content!!
}