package com.github.frtu.workflow.serverlessworkflow.state

import io.serverlessworkflow.api.actions.Action
import io.serverlessworkflow.api.states.DefaultState.Type.OPERATION
import io.serverlessworkflow.api.states.OperationState

/**
 * Builder for operation state
 *
 * @author frtu
 * @since 1.2.5
 */
class OperationStateBuilder(
    name: String? = null,
) : AbstractStateBuilder<OperationState>(OperationState(), OPERATION, name) {
    private val actions = mutableListOf<Action>()

    operator fun Action.unaryPlus() {
        logger.trace("action: name={}", this.name)
        actions += this
    }

    override fun build(): OperationState =
        model.withActions(actions)
}

fun operation(name: String? = null, options: OperationStateBuilder.() -> Unit = {}): OperationState =
    OperationStateBuilder(name).apply(options).build()

class ActionBuilder(name: String? = null) {
    private val model: Action = Action()

    init {
        assignName(name)
    }

    var name: String
        get() = model.name
        set(value) {
            assignName(value)
        }

    private fun assignName(value: String?) {
        value?.let { model.withName(value) }
    }

    fun build(): Action = model
}

fun action(name: String? = null, options: ActionBuilder.() -> Unit = {}): Action =
    ActionBuilder(name).apply(options).build()
