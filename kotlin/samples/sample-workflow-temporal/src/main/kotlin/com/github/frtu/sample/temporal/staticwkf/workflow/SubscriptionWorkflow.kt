package com.github.frtu.sample.temporal.staticwkf.workflow

import com.github.frtu.sample.temporal.staticwkf.SubscriptionEvent
import io.temporal.workflow.WorkflowInterface
import io.temporal.workflow.WorkflowMethod

@WorkflowInterface
interface SubscriptionWorkflow {
    @WorkflowMethod
    fun start(subscriptionEvent: SubscriptionEvent)
}

const val TASK_QUEUE_SUBSCRIPTION = "TASK_QUEUE_SUBSCRIPTION"
