package com.github.frtu.sample.temporal.staticwkf.starter

import com.github.frtu.sample.temporal.staticwkf.SubscriptionEvent
import java.util.*

interface SubscriptionHandler {
    fun handle(subscriptionEvent: SubscriptionEvent): UUID
}