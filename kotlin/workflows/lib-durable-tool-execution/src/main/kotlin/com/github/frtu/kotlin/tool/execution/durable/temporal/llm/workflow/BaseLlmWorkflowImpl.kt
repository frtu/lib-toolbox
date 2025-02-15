package com.github.frtu.kotlin.tool.execution.durable.temporal.llm.workflow

import com.github.frtu.kotlin.ai.os.memory.Conversation
import com.github.frtu.kotlin.tool.execution.durable.temporal.llm.activity.LlmActivity
import io.temporal.activity.ActivityOptions
import io.temporal.common.RetryOptions
import io.temporal.workflow.Workflow
import java.time.Duration

/**
 * Encapsulate an LLM executor `LlmActivityImpl` as an abstract Workflow.
 *
 * Need to extend class and configure a systemPrompt
 *
 * @author Frédéric TU
 * @since 2.0.16
 */
abstract class BaseLlmWorkflowImpl(
    private val systemPrompt: String,
    /** Activity task queue that can be the same with Workflow */
    private val activityTaskQueue: String,
    private val activityExecutionTimeout: Duration? = Duration.ofSeconds(5), // Timeout options specify when to automatically timeout Activities if the process is taking too long.
    private val activityRetryOptions: RetryOptions? = RetryOptions {
        setInitialInterval(Duration.ofMillis(100))
        setMaximumInterval(Duration.ofSeconds(10))
        setBackoffCoefficient(2.0)
        setMaximumAttempts(10)
    },
    private val llmActivity: LlmActivity = Workflow.newActivityStub(
        LlmActivity::class.java,
        ActivityOptions {
            setTaskQueue(activityTaskQueue)
            activityExecutionTimeout?.let { setStartToCloseTimeout(it) }
            activityRetryOptions?.let { setRetryOptions(it) }
        })
) : LlmWorkflow {
    override fun chat(userMessage: String): String =
        llmActivity.chat(Conversation(systemPrompt).user(userMessage))!!
}