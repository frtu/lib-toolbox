package com.github.frtu.sample.temporal.workflow

import com.github.frtu.logs.core.StructuredLogger
import com.github.frtu.sample.temporal.activity.EmailSinkActivity
import com.github.frtu.sample.temporal.activity.TASK_QUEUE_EMAIL
import com.github.frtu.workflow.temporal.annotation.WorkflowImplementation
import io.temporal.activity.ActivityOptions
import io.temporal.common.RetryOptions
import io.temporal.workflow.Workflow
import java.time.Duration

@WorkflowImplementation(taskQueue = TASK_QUEUE_SUBSCRIPTION)
class SubscriptionWorkflowImpl : SubscriptionWorkflow {
    override fun start() {
        println("start")
    }

    private val sendEmailActivity = Workflow.newActivityStub(
        EmailSinkActivity::class.java,
        ActivityOptions {
            // ActivityOptions DSL
            setTaskQueue(TASK_QUEUE_EMAIL)
            setStartToCloseTimeout(Duration.ofSeconds(5)) // Timeout options specify when to automatically timeout Activities if the process is taking too long.
            // Temporal retries failures by default, this is simply an example.
            setRetryOptions(// RetryOptions specify how to automatically handle retries when Activities fail.
                RetryOptions {
                    setInitialInterval(Duration.ofMillis(100))
                    setMaximumInterval(Duration.ofSeconds(10))
                    setBackoffCoefficient(2.0)
                    setMaximumAttempts(10)
                })
        },
        // ActivityStubs enable calls to methods as if the Activity object is local, but actually perform an RPC.
        mapOf(
            SUBSCRIPTION to ActivityOptions { setHeartbeatTimeout(Duration.ofSeconds(5)) }
        )
    )

    private val structuredLogger = StructuredLogger.create(this::class.java)

    companion object {
        private const val SUBSCRIPTION = "Subscription"
    }
}
