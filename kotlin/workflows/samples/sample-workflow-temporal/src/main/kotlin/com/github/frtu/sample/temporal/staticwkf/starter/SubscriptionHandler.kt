package com.github.frtu.sample.temporal.staticwkf.starter

import com.github.frtu.sample.sink.EmailDetail
import com.github.frtu.sample.temporal.staticwkf.SubscriptionEvent
import java.util.*

interface SubscriptionHandler {
    fun handle(subscriptionEvent: SubscriptionEvent): UUID
    fun query(workflowId: String): EmailDetail?
    fun terminate(workflowId: String)
    fun fail(workflowId: String)
}
