package com.github.frtu.sample.temporal.staticwkf.workflow

import com.github.frtu.sample.sink.EmailDetail
import com.github.frtu.sample.temporal.staticwkf.SubscriptionEvent
import io.temporal.workflow.QueryMethod
import io.temporal.workflow.SignalMethod
import io.temporal.workflow.WorkflowInterface
import io.temporal.workflow.WorkflowMethod

@WorkflowInterface
interface SubscriptionWorkflow {
    @WorkflowMethod
    fun start(subscriptionEvent: SubscriptionEvent)

    @QueryMethod
    fun queryEmailDetail(): EmailDetail?

    @SignalMethod
    fun acknowledge(acknowledgeSignal: AcknowledgeSignal)
}

const val TASK_QUEUE_SUBSCRIPTION = "TASK_QUEUE_SUBSCRIPTION"
