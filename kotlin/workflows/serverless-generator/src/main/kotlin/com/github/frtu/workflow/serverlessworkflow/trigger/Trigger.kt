package com.github.frtu.workflow.serverlessworkflow.trigger

import io.serverlessworkflow.api.interfaces.State

abstract class Trigger(val category: TriggerCategory) {

    open fun toState(): State? = null
}

enum class TriggerCategory {
    BY_EVENT,
    BY_TIME,
}