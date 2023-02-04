package com.github.frtu.workflow.serverlessworkflow.trigger

import com.github.frtu.workflow.serverlessworkflow.DslBuilder

/**
 * Trigger aggregator for workflow DSL
 *
 * @author frtu
 * @since 1.2.5
 */
@DslBuilder
class AllTriggersBuilder {
    private val triggers = mutableListOf<Trigger<*>>()

    @DslBuilder
    operator fun Trigger<*>.unaryPlus() {
        triggers += this
    }

    fun build(): MutableList<Trigger<*>> = triggers
}