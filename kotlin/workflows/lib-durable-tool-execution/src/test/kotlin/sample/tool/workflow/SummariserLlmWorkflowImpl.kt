package sample.tool.workflow

import com.github.frtu.kotlin.tool.execution.durable.temporal.llm.workflow.BaseLlmWorkflowImpl

/**
 * Sample workflow containing its own systemPrompt
 *
 * @author Frédéric TU
 * @since 2.0.16
 */
open class SummariserLlmWorkflowImpl : BaseLlmWorkflowImpl(
    systemPrompt = """
        Summarize the current discussion, focusing on the main topics and specific details provided.
        Highlight the user’s requests, objectives, and preferences, including any recurring themes or ongoing projects.
        Ensure the summary is concise, accurate, and structured to give a clear overview of the context and progress of the conversation.
    """.trimIndent(),
    activityTaskQueue = TASK_QUEUE,
) {
    companion object {
        /** Workflow task queue */
        const val TASK_QUEUE = "LLM_SUMMARISER_TASK_QUEUE"
    }
}