package com.github.frtu.kotlin.tool.execution.durable.temporal.workflow

import com.fasterxml.jackson.databind.JsonNode
import io.temporal.workflow.WorkflowInterface
import io.temporal.workflow.WorkflowMethod

/**
 * `Workflow Type` for base Tool orchestration graph that you can extend and specialise
 *
 * @author Frédéric TU
 * @since 2.0.8
 */
@WorkflowInterface
interface ToolWorkflow {
    @WorkflowMethod
    fun execute(parameter: JsonNode): JsonNode
}