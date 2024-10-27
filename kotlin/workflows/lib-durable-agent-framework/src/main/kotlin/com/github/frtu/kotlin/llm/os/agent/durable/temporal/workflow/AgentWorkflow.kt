package com.github.frtu.kotlin.llm.os.agent.durable.temporal.workflow

import com.fasterxml.jackson.databind.JsonNode
import io.temporal.workflow.WorkflowInterface
import io.temporal.workflow.WorkflowMethod

/**
 * Agent orchestration graph
 *
 * @author Frédéric TU
 * @since 2.0.7
 */
@WorkflowInterface
interface AgentWorkflow {
    @WorkflowMethod
    fun execute(parameter: JsonNode): JsonNode
}