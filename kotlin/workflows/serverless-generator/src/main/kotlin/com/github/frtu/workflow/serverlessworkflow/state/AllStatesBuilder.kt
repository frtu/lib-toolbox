package com.github.frtu.workflow.serverlessworkflow.state

import io.serverlessworkflow.api.states.DefaultState
import io.serverlessworkflow.api.states.SleepState

/**
 * states builder and aggregator for workflow DSL
 *
 * @author frtu
 * @since 1.2.5
 */
class AllStatesBuilder {
    private val states = mutableListOf<DefaultState>()

    operator fun DefaultState.unaryPlus() {
        states += this
    }

    fun build(): MutableList<DefaultState> = states
}

fun sleep(stateName: String? = null, duration: String? = null, options: SleepStateBuilder.() -> Unit = {}): SleepState =
    SleepStateBuilder(stateName).apply {
        duration?.let { this.duration = duration }
    }.apply(options).build()