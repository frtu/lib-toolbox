package com.github.frtu.workflow.serverlessworkflow.trigger

import io.serverlessworkflow.api.interfaces.State

abstract class Trigger<ITEM>(val category: TriggerCategory) {
    open fun toResult(): List<ITEM> = emptyList()
}

enum class TriggerCategory {
    BY_EVENT,
    BY_TIME,
}