package com.github.frtu.kotlin.tool.execution.durable.temporal.llm.activity

import io.temporal.activity.ActivityInterface
import io.temporal.activity.ActivityMethod

/**
 * Encapsulate an LLM executor and a prompt as an Activity
 *
 * @author Frédéric TU
 * @since 2.0.16
 */
@ActivityInterface
interface SpecialisedAgentActivity {
    @ActivityMethod
    fun message(userMessage: String): String

    companion object {
        /** Each Agent should have his dedicated TASK_QUEUE name */
        // const val TASK_QUEUE = "SPECIALISED_NAMED_TASK_QUEUE"
    }
}