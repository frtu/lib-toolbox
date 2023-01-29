package com.github.frtu.workflow.serverlessworkflow.workflow

import com.github.frtu.workflow.serverlessworkflow.DslBuilder
import com.github.frtu.workflow.serverlessworkflow.state.AllStatesBuilder
import com.github.frtu.workflow.serverlessworkflow.state.OperationStateBuilder
import com.github.frtu.workflow.serverlessworkflow.trigger.AllTriggersBuilder
import com.github.frtu.workflow.serverlessworkflow.trigger.EventTriggerBuilder
import com.github.frtu.workflow.serverlessworkflow.trigger.Trigger
import io.serverlessworkflow.api.end.End
import io.serverlessworkflow.api.interfaces.State
import io.serverlessworkflow.api.start.Start
import io.serverlessworkflow.api.states.DefaultState
import io.serverlessworkflow.api.states.EventState
import io.serverlessworkflow.api.states.OperationState
import io.serverlessworkflow.api.validation.ValidationError
import io.serverlessworkflow.api.workflow.Events
import io.serverlessworkflow.api.workflow.Functions
import io.serverlessworkflow.validation.WorkflowValidatorImpl
import org.slf4j.LoggerFactory
import java.util.UUID
import io.serverlessworkflow.api.Workflow as ServerlessWorkflow

/**
 * Base class for workflow Builder
 *
 * @author frtu
 * @since 1.2.5
 */
@DslBuilder
open class WorkflowBuilder(
    name: String? = null,
) {
    private val workflow: ServerlessWorkflow = ServerlessWorkflow()
        .withId(UUID.randomUUID().toString())
        .withVersion("0.1.0")
        .withExpressionLang("spel")

    private val triggers = mutableListOf<Trigger>()

    init {
        assignName(name)
    }

    @DslBuilder
    var name: String?
        get() = workflow.name
        set(value) {
            assignName(value)
        }

    private fun assignName(value: String?) = value?.let { workflow.withName(value) }

    private val statesList = mutableListOf<State>()
    private val defaultStatesList = mutableListOf<DefaultState>()

    fun append(moreStates: List<State>) = statesList.addAll(moreStates)

    @DslBuilder
    var start: String?
        get() = workflow.start.stateName
        set(value) {
            assignStart(value)
        }

    private fun assignStart(value: String?) = value?.let { workflow.withStart(Start().withStateName(value)) }

    @DslBuilder
    fun triggered(options: AllTriggersBuilder.() -> Unit) {
        // Init
        val triggerBuilder = AllTriggersBuilder()
        triggerBuilder.apply(options)

        // Apply
        val allTriggers = triggerBuilder.build()
        logger.debug("build triggers: size=${allTriggers.size}")
        val triggerStates = allTriggers.mapNotNull { it.toState() }
        if (triggerStates.size > 1) {
            assignStart(triggerStates[0].name)
        }
        append(triggerStates)
    }

    @DslBuilder
    fun states(options: AllStatesBuilder.() -> Unit) {
        // Init
        val stateBuilder = AllStatesBuilder()
        stateBuilder.apply(options)

        // Apply
        val allStates = stateBuilder.build()
        logger.debug("build states: size=${allStates.size}")
        defaultStatesList.addAll(allStates)
    }

    open fun build(): ServerlessWorkflow = workflow.apply {
        // Post construct
        val stateValidationErrors = mutableListOf<ValidationError>()
        if (defaultStatesList.isNotEmpty()) {
            // Allow to fix state using DefaultState
            val lastState = defaultStatesList.last()
            lastState.withEnd(End().withTerminate(true))

            // Append to the final list
            statesList.addAll(defaultStatesList)
        }
        if (statesList.isNotEmpty()) {
            this.withStates(statesList.toList())

            val operationStates = states.filterIsInstance<OperationState>()
            if (operationStates.isNotEmpty()) {
                this.withFunctions(Functions(
                    operationStates.flatMap { state ->
                        OperationStateBuilder.buildFunctionDefinition(state)
                    }.distinctBy { it.name }
                ))
            }

            val eventStates = states.filterIsInstance<EventState>()
            if (eventStates.isNotEmpty()) {
                this.withEvents(Events(
                    eventStates.flatMap { state ->
                        EventTriggerBuilder.getEventDefinition(state)
                    }.distinctBy { it.type }
                ))
            }
            if (eventStates.size > 1) {
                stateValidationErrors.add(schemaValidationError(" trigger by multiple events not supported yet"))
            }
        }

        // Validation
        logger.trace("Generate ${workflow}")
        val validationErrors = WorkflowValidatorImpl().setWorkflow(workflow).validate().toMutableList()
        if (workflow.name.isNullOrBlank()) {
            validationErrors.add(0, schemaValidationError(" workflow.name should not be blank"))
        }
        validationErrors.addAll(stateValidationErrors)
        if (validationErrors.isNotEmpty()) {
            throw IllegalArgumentException(validationErrors.joinToString("\n"))
        }
    }

    private fun schemaValidationError(message: String) = ValidationError().apply {
        this.type = ValidationError.SCHEMA_VALIDATION
        this.message = message
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}