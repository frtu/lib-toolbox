package com.github.frtu.workflow.serverlessworkflow.state

import com.github.frtu.workflow.serverlessworkflow.DslBuilder
import io.serverlessworkflow.api.interfaces.State
import io.serverlessworkflow.api.states.DefaultState
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
    val defaultStates: MutableList<DefaultState> = mutableListOf()
) {

    @DslBuilder
    operator fun DefaultState.unaryPlus() {
        defaultStates += this
    }

    fun append(moreStates: List<State>) = states.addAll(moreStates)
}
