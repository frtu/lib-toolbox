package com.github.frtu.sample.temporal.workflow

import io.temporal.workflow.WorkflowInterface
import io.temporal.workflow.WorkflowMethod

@WorkflowInterface
interface SubscriptionWorkflow {
    @WorkflowMethod
    fun start()
}

const val TASK_QUEUE_SUBSCRIPTION = "TASK_QUEUE_SUBSCRIPTION"
