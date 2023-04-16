package com.github.frtu.sample.temporal.staticwkf.workflow

import com.github.frtu.sample.sink.EmailDetail

data class SubscriptionState(val subscriptionId: String) {
    var emailDetail: EmailDetail? = null
    var status: Status = Status.RUNNING
}

enum class Status {
    RUNNING, COMPLETED, FAILED
}
