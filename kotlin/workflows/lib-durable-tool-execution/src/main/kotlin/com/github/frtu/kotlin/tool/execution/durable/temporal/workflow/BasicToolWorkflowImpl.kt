package com.github.frtu.kotlin.tool.execution.durable.temporal.workflow

import com.fasterxml.jackson.databind.JsonNode
import com.github.frtu.kotlin.tool.execution.durable.temporal.activity.ToolAsActivity
import io.temporal.activity.ActivityOptions
import io.temporal.common.RetryOptions
import io.temporal.workflow.Workflow
import java.time.Duration

/**
 * Basic `ToolWorkflow` that only calls one activity
 *
 * @author Frédéric TU
 * @since 2.0.8
 */
open class BasicToolWorkflowImpl(
    /** Activity task queue that can be the same with Workflow */
    private val activityTaskQueue: String = TASK_QUEUE,
    private val activityExecutionTimeout: Duration? = Duration.ofSeconds(5), // Timeout options specify when to automatically timeout Activities if the process is taking too long.
    private val activityRetryOptions: RetryOptions? = RetryOptions {
        setInitialInterval(Duration.ofMillis(100))
        setMaximumInterval(Duration.ofSeconds(10))
        setBackoffCoefficient(2.0)
        setMaximumAttempts(10)
    }
) : ToolWorkflow {
    companion object {
        /** Workflow task queue */
        const val TASK_QUEUE = "TOOL_TASK_QUEUE"
    }

    override fun execute(parameter: JsonNode): JsonNode {
        return toolAsActivity.doExecute(parameter)
    }

    private val toolAsActivity = Workflow.newActivityStub(
        ToolAsActivity::class.java,
        ActivityOptions {
            setTaskQueue(activityTaskQueue)
            activityExecutionTimeout?.let { setStartToCloseTimeout(it) }
            activityRetryOptions?.let { setRetryOptions(it) }
        })
}