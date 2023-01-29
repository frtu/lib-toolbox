package com.github.frtu.workflow.serverlessworkflow.trigger

open class Trigger(val category: TriggerCategory)

enum class TriggerCategory {
    BY_EVENT,
    BY_TIME,
}