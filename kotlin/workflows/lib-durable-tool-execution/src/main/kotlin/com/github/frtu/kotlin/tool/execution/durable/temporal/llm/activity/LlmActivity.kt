package com.github.frtu.kotlin.tool.execution.durable.temporal.llm.activity

import com.github.frtu.kotlin.ai.os.memory.Conversation
import io.temporal.activity.ActivityInterface
import io.temporal.activity.ActivityMethod

/**
 * Encapsulate an LLM executor as an Activity
 *
 * @author Frédéric TU
 * @since 2.0.16
 */
@ActivityInterface
interface LlmActivity {
    @ActivityMethod
    fun chat(conversation: Conversation): String

    companion object {
        /** Workflow task queue */
        const val TASK_QUEUE = "LLM_TASK_QUEUE"
    }
}