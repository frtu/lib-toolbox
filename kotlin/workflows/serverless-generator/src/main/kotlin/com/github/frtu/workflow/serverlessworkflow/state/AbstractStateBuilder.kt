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
    name: String? = null,
) {
    init {
        state.withType(type)
        assignName(name)
    }

    var name: String
        get() = state.name
        set(value) {
            assignName(value)
        }

    private fun assignName(value: String?) = value?.let { state.withName(it) }

    var transition: String?
        get() = state.transition.nextState
        set(value) {
            assignTransition(value)
        }

    private fun assignTransition(value: String?) = value?.let { state.withTransition(Transition(it)) }

    open fun build(): STATE = state

    protected val logger: Logger = LoggerFactory.getLogger(this::class.java)
}