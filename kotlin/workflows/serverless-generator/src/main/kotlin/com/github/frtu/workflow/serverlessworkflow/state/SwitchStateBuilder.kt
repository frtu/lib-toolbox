package com.github.frtu.workflow.serverlessworkflow.state

import io.serverlessworkflow.api.defaultdef.DefaultConditionDefinition
import io.serverlessworkflow.api.states.DefaultState.Type.SWITCH
import io.serverlessworkflow.api.states.SwitchState
import io.serverlessworkflow.api.switchconditions.DataCondition
import io.serverlessworkflow.api.transitions.Transition

/**
 * Builder for switch state
 *
 * @author frtu
 * @since 1.2.5
 */
class SwitchStateBuilder(
    name: String? = null,
) : AbstractStateBuilder<SwitchState>(SwitchState(), SWITCH, name) {
    private val dataConditions = mutableListOf<DataCondition>()
    private val defaultCondition = DefaultConditionDefinition()

    operator fun DataCondition.unaryPlus() {
        logger.trace("case: name={} condition={} transition={}", this.name, this.condition, this.transition?.nextState)
        dataConditions += this
    }

    fun default(transition: String): DefaultConditionDefinition {
        logger.trace("default: transition={}", transition)
        return defaultCondition.withTransition(Transition(transition))
    }

    override fun build(): SwitchState =
        model.withDataConditions(dataConditions).withDefaultCondition(defaultCondition)
}

fun switch(name: String? = null, options: SwitchStateBuilder.() -> Unit = {}): SwitchState =
    SwitchStateBuilder(name).apply(options).build()

class CaseBuilder(condition: String, name: String? = null, transition: String? = null) :
    AbstractBuilder<DataCondition>(
        DataCondition().withCondition(condition),
        name,
        transition,
    ) {
    init {
        assignTransition(transition)
    }

    val condition: String = model.condition

    override var name: String
        get() = model.name
        set(value) {
            assignName(value)
        }

    override fun assignName(value: String?) {
        value?.let { model.withName(value) }
    }

    override var transition: String?
        get() = model.transition?.nextState
        set(value) {
            assignTransition(value)
        }

    override fun assignTransition(value: String?) {
        value?.let { model.withTransition(Transition(it)) }
    }
}

fun case(condition: String, name: String? = null, options: CaseBuilder.() -> Unit = {}): DataCondition =
    CaseBuilder(condition, name).apply(options).build()
