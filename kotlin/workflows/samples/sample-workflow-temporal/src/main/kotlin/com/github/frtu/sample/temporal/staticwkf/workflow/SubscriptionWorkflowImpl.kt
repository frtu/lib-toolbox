package com.github.frtu.sample.temporal.staticwkf.workflow

import com.github.frtu.logs.core.RpcLogger.requestBody
import com.github.frtu.logs.core.StructuredLogger
import com.github.frtu.logs.core.StructuredLogger.*
import com.github.frtu.sample.persistence.basic.STATUS
import com.github.frtu.sample.sink.EmailDetail
import com.github.frtu.sample.temporal.activity.EmailSinkActivity
import com.github.frtu.sample.temporal.activity.TASK_QUEUE_EMAIL
import com.github.frtu.sample.temporal.staticwkf.SubscriptionEvent
import com.github.frtu.workflow.temporal.annotation.WorkflowImplementation
import io.temporal.activity.ActivityOptions
import io.temporal.common.RetryOptions
import io.temporal.workflow.Workflow
import java.time.Duration
import java.util.*

@WorkflowImplementation(taskQueue = TASK_QUEUE_SUBSCRIPTION)
class SubscriptionWorkflowImpl : SubscriptionWorkflow {
    override fun start(subscriptionEvent: SubscriptionEvent) {
        structuredLogger.info(flowId(subscriptionEvent.id), phase("STARTING_ACTIVITY"), requestBody(subscriptionEvent))
        sendEmailActivity.emit(
            EmailDetail(
                receiver = "receiver@domain.com",
                subject = "Confirmation of Subscription ID : ${subscriptionEvent.id}",
                content = "Subscription Type ${subscriptionEvent.type} with data : ${subscriptionEvent.data}",
                status = STATUS.INIT.toString(),
            )
        )
        structuredLogger.info(flowId(subscriptionEvent.id), phase("STARTED_ACTIVITY"))
    }

    private val sendEmailActivity = Workflow.newActivityStub(
        EmailSinkActivity::class.java,
        ActivityOptions {
            // ActivityOptions DSL
            setTaskQueue(TASK_QUEUE_EMAIL)
            setStartToCloseTimeout(Duration.ofSeconds(5)) // Timeout options specify when to automatically timeout Activities if the process is taking too long.
            // Temporal retries failures by default, this is simply an example.
            setRetryOptions( // RetryOptions specify how to automatically handle retries when Activities fail.
                RetryOptions {
                    setDoNotRetry(
                        IllegalArgumentException::class.qualifiedName,
                    )
                    setInitialInterval(Duration.ofMillis(100))
                    setMaximumInterval(Duration.ofSeconds(10))
                    setBackoffCoefficient(2.0)
                    setMaximumAttempts(10)
                }
            )
        },
        // ActivityStubs enable calls to methods as if the Activity object is local, but actually perform an RPC.
        mapOf(
            SUBSCRIPTION to ActivityOptions { setHeartbeatTimeout(Duration.ofSeconds(5)) }
        )
    )

    private fun fetchUserIds(subscriptionEvent: SubscriptionEvent) = (1..2)
        .map { UUID.randomUUID() }
        .toCollection(mutableListOf())

    private val logger = Workflow.getLogger(SubscriptionWorkflowImpl::class.java)
    private val structuredLogger = StructuredLogger.create(this::class.java)

    companion object {
        private const val SUBSCRIPTION = "Subscription"
    }
}
