package com.github.frtu.sample.temporal.staticwkf.workflow

import com.github.frtu.logs.core.RpcLogger.requestBody
import com.github.frtu.logs.core.RpcLogger.responseBody
import com.github.frtu.logs.core.StructuredLogger
import com.github.frtu.logs.core.StructuredLogger.*
import com.github.frtu.sample.temporal.staticwkf.SubscriptionEvent
import io.temporal.workflow.Workflow
import java.util.*

class SubscriptionWorkflowImpl : SubscriptionWorkflow {
    override fun start(subscriptionEvent: SubscriptionEvent) {
        // Fetch users & startReminder to all of them
        val childReminderWorkflowList = fetchUserIds(subscriptionEvent)
            .map {
                structuredLogger.info(flowId(subscriptionEvent.id), key("user_id", it), phase("STARTING_REMINDER"), requestBody(subscriptionEvent))
            }.toCollection(mutableListOf())

        structuredLogger.info(flowId(subscriptionEvent.id), phase("STARTED_REMINDER"), responseBody(childReminderWorkflowList.size))
        // Do some tasks
        Workflow.sleep(3_000)

        structuredLogger.info(flowId(subscriptionEvent.id), phase("REMINDER_STARTED"))
    }

    private fun fetchUserIds(subscriptionEvent: SubscriptionEvent) = (1..2)
        .map { UUID.randomUUID() }
        .toCollection(mutableListOf())

    private val logger = Workflow.getLogger(this::class.java)
    private val structuredLogger = StructuredLogger.create(logger)
}
