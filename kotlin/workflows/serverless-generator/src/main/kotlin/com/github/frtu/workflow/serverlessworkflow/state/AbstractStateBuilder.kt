package com.github.frtu.workflow.serverlessworkflow.state

import com.github.frtu.workflow.serverlessworkflow.DslBuilder
import io.serverlessworkflow.api.states.DefaultState
import io.serverlessworkflow.api.transitions.Transition

/**
 * Base class for Builder state
 *
 * @author frtu
 * @since 1.2.5
 */
@DslBuilder
abstract class AbstractStateBuilder<STATE : DefaultState>(
    state: STATE,
    type: DefaultState.Type,
    name: String? = null,
) : AbstractBuilder<STATE>(state, name) {
    init {
        model.withType(type)
    }

    @DslBuilder
    override var name: String
        get() = model.name
        set(value) { assignName(value) }

    override fun assignName(value: String?) {
        value?.let { model.withName(it) }
    }

    @DslBuilder
    override var transition: String?
        get() = model.transition?.nextState
        set(value) { assignTransition(value) }

    override fun assignTransition(value: String?) {
        value?.let { model.withTransition(Transition(it)) }
    }
}