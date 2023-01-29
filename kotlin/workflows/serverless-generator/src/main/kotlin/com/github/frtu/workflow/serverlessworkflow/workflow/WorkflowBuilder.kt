package com.github.frtu.workflow.serverlessworkflow.workflow

import com.github.frtu.workflow.serverlessworkflow.state.AllStatesBuilder
import com.github.frtu.workflow.serverlessworkflow.state.OperationStateBuilder
import io.serverlessworkflow.api.end.End
import io.serverlessworkflow.api.states.DefaultState
import io.serverlessworkflow.api.validation.ValidationError
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
open class WorkflowBuilder(
    name: String? = null,
) {
    private val workflow: ServerlessWorkflow = ServerlessWorkflow()
        .withId(UUID.randomUUID().toString())
        .withVersion("0.1.0")
        .withExpressionLang("spel")

    init {
        assignName(name)
    }

    var name: String?
        get() = workflow.name
        set(value) {
            assignName(value)
        }

    private fun assignName(value: String?) = value?.let { workflow.withName(value) }

    private val statesList = mutableListOf<DefaultState>()

    fun states(options: AllStatesBuilder.() -> Unit) {
        // Init
        val stateBuilder = AllStatesBuilder()
        stateBuilder.apply(options)

        // Apply
        val defaultStates = stateBuilder.build()
        logger.debug("build states: size=${defaultStates.size}")
        statesList.addAll(defaultStates)
    }

    open fun build(): ServerlessWorkflow = workflow.apply {
        // Post construct
        if (statesList.isNotEmpty()) {
            val lastState = statesList.last()
            lastState.withEnd(End().withTerminate(true))

            this.withStates(statesList.toList())
            this.withFunctions(Functions(
                states.flatMap { state ->
                    OperationStateBuilder.buildFunctionDefinition(state)
                }.distinctBy { it.name }
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