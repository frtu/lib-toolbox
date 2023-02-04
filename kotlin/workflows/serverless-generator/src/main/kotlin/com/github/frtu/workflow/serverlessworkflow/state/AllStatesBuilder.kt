package com.github.frtu.workflow.serverlessworkflow.state

import com.github.frtu.workflow.serverlessworkflow.DslBuilder
import io.serverlessworkflow.api.interfaces.State
import io.serverlessworkflow.api.states.DefaultState
import io.serverlessworkflow.api.states.OperationState
import io.serverlessworkflow.api.states.SleepState

/**
 * states builder and aggregator for workflow DSL
 *
 * @author frtu
 * @since 1.2.5
 */
@DslBuilder
class AllStatesBuilder(
    val states: MutableList<State> = mutableListOf(),
) {

    @DslBuilder
    operator fun State.unaryPlus() {
        states += this
    }

    fun append(externalBuiltStates: List<SleepState>) = states.addAll(externalBuiltStates)

    fun build(): List<State> = states
}
