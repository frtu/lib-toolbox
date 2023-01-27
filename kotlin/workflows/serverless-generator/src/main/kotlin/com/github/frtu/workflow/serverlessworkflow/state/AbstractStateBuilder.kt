package com.github.frtu.workflow.serverlessworkflow.state

import io.serverlessworkflow.api.states.DefaultState
import io.serverlessworkflow.api.transitions.Transition
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Base class for Builder state
 *
 * @author frtu
 * @since 1.2.5
 */
abstract class AbstractStateBuilder<STATE : DefaultState>(
    protected val state: STATE,
    type: DefaultState.Type,
    stateName: String? = null,
) {
    init {
        state.withType(type)
        stateName?.let { assignStateName(it) }
    }

    var stateName: String
        get() = state.name
        set(value) {
            assignStateName(value)
        }

    private fun assignStateName(value: String) {
        state.withName(value)
    }

    fun assignTransition(value: String) {
        state.withTransition(Transition(value))
    }

    open fun build(): STATE = state

    protected val logger: Logger = LoggerFactory.getLogger(this::class.java)
}