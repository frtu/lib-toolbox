package com.github.frtu.kotlin.llm.os.agent.durable.temporal.activity

import com.fasterxml.jackson.databind.JsonNode
import com.github.frtu.kotlin.action.execution.GenericAction
import com.github.frtu.kotlin.llm.os.tool.Tool
import kotlinx.coroutines.runBlocking

/**
 * Encapsulate a Tool execution into an Activity
 *
 * @author Frédéric TU
 * @since 2.0.8
 */
open class ToolAsActivityImpl(
    private val tool: Tool,
) : ToolAsActivity, GenericAction {
    override suspend fun execute(parameter: JsonNode): JsonNode = tool.execute(parameter)

    /**
     * Adapter method for Temporal suspend function
     */
    override fun doExecute(parameter: JsonNode): JsonNode = runBlocking {
        execute(parameter)
    }
}