package com.github.frtu.kotlin.tool.execution.durable.temporal.llm.workflow

import io.temporal.workflow.WorkflowInterface
import io.temporal.workflow.WorkflowMethod

/**
 * Encapsulate an LLM executor as a Workflow
 *
 * @author Frédéric TU
 * @since 2.0.16
 */
@WorkflowInterface
interface LlmWorkflow {
    @WorkflowMethod
    fun chat(userMessage: String): String
}