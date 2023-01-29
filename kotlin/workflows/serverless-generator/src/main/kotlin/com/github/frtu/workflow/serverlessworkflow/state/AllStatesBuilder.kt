package com.github.frtu.workflow.serverlessworkflow.state

import com.github.frtu.workflow.serverlessworkflow.DslBuilder
import io.serverlessworkflow.api.states.DefaultState
import io.serverlessworkflow.api.states.SleepState

/**
 * states builder and aggregator for workflow DSL
 *
 * @author frtu
 * @since 1.2.5
 */
@DslBuilder
class AllStatesBuilder {
    private val states = mutableListOf<DefaultState>()

    @DslBuilder
    operator fun DefaultState.unaryPlus() {
        states += this
    }

    fun build(): MutableList<DefaultState> = states
}
