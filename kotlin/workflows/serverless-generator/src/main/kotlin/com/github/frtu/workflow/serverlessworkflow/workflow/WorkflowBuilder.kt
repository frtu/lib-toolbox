package com.github.frtu.workflow.serverlessworkflow.workflow

import com.github.frtu.workflow.serverlessworkflow.DslBuilder
import com.github.frtu.workflow.serverlessworkflow.state.AllStatesBuilder
import com.github.frtu.workflow.serverlessworkflow.state.OperationStateBuilder
import com.github.frtu.workflow.serverlessworkflow.trigger.EventTriggerBuilder
import com.github.frtu.workflow.serverlessworkflow.trigger.Trigger
import io.serverlessworkflow.api.end.End
import io.serverlessworkflow.api.interfaces.State
import io.serverlessworkflow.api.states.DefaultState
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

    @DslBuilder
    operator fun Trigger.unaryPlus() {
        logger.trace("trigger: type={}", this.category)
        triggers += this
    }

    private fun assignName(value: String?) = value?.let { workflow.withName(value) }

    private val statesList = mutableListOf<State>()
    private val defaultStatesList = mutableListOf<DefaultState>()

    fun append(moreStates: List<State>) = statesList.addAll(moreStates)


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
        if (defaultStatesList.isNotEmpty()) {
            // Allow to fix state using DefaultState
            val lastState = defaultStatesList.last()
            lastState.withEnd(End().withTerminate(true))

            // Append to the final list
            statesList.addAll(defaultStatesList)
        }
        if (statesList.isNotEmpty()) {
            this.withStates(statesList.toList())
            this.withFunctions(Functions(
                states.flatMap { state ->
                    OperationStateBuilder.buildFunctionDefinition(state)
                }.distinctBy { it.name }
            ))
            this.withEvents(Events(
                states.flatMap { state ->
                    EventTriggerBuilder.getEventDefinition(state)
                }.distinctBy { it.type }
            ))
        }

        // Validation
        logger.trace("Generate ${workflow}")
        val validationErrors = WorkflowValidatorImpl().setWorkflow(workflow).validate().toMutableList()
        if (workflow.name.isNullOrBlank()) {
            validationErrors.add(0, schemaValidationError(" workflow.name should not be blank"))
        }
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