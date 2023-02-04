package com.github.frtu.workflow.serverlessworkflow.workflow

import com.github.frtu.kotlin.utils.io.toJsonString
import com.github.frtu.workflow.serverlessworkflow.DslBuilder
import com.github.frtu.workflow.serverlessworkflow.state.AllStatesBuilder
import com.github.frtu.workflow.serverlessworkflow.state.OperationStateBuilder
import com.github.frtu.workflow.serverlessworkflow.trigger.AllTriggersBuilder
import com.github.frtu.workflow.serverlessworkflow.trigger.EventTriggerBuilder
import com.github.frtu.workflow.serverlessworkflow.trigger.TimeTrigger
import io.serverlessworkflow.api.interfaces.State
import io.serverlessworkflow.api.schedule.Schedule
import io.serverlessworkflow.api.start.Start
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
    val treeBuilder: TreeBuilder = TreeBuilder(),
) {
    private val workflow: ServerlessWorkflow = ServerlessWorkflow()
        .withId(UUID.randomUUID().toString())
        .withVersion("0.1.0")
        .withExpressionLang("spel")

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

    private val aggregatedStates = mutableListOf<State>()

    @DslBuilder
    var start: String?
        get() = workflow.start.stateName
        set(value) {
            assignStart(value)
        }

    private fun assignStart(name: String?, schedule: Schedule? = null) {
        val start = if (workflow.start == null) Start() else workflow.start
        start.apply {
            name?.let {
                withStateName(it)
            }
            schedule?.let<Schedule, Unit> {
                withSchedule(it)
            }
        }
        workflow.withStart(start)
    }

    @DslBuilder
    fun triggered(options: AllTriggersBuilder.() -> Unit) {
        // Init
        val allTriggers = AllTriggersBuilder().apply(options).build()
        val triggerResults = allTriggers.flatMap { it.toResult() }

        // Apply
        logger.debug("build triggers: size=${triggerResults.size} triggerResults:$triggerResults")

        // Trigger with state
        val triggerStates = triggerResults.filterIsInstance<State>()
        if (triggerStates.isNotEmpty()) {
            val startTransition = triggerStates[0].transition.nextState
            logger.debug("Start: transition=$startTransition")
            assignStart(startTransition)
        }

        // TimeTrigger
        val timeTriggers = triggerResults.filterIsInstance<TimeTrigger>()
        if (timeTriggers.size == 1) {
            val timeTrigger = timeTriggers[0]
            logger.debug("Start: transition=${timeTrigger.transition} and schedule:${timeTrigger.schedule}")
            assignStart(timeTrigger.transition, timeTrigger.schedule)
        } else if (timeTriggers.size > 1) {
            throw IllegalArgumentException("Should not have more than 1 triggers! triggers:$timeTriggers")
        }
        aggregatedStates.addAll(triggerStates)
    }

    @DslBuilder
    fun states(options: AllStatesBuilder.() -> Unit) {
        // Init
        val stateBuilder = AllStatesBuilder().apply(options)
        // Apply
        with(stateBuilder.states) {
            logger.debug("merge states from builder: size=$size")
            aggregatedStates.addAll(this)
        }
    }

    open fun build(): ServerlessWorkflow = workflow.apply {
        // Post construct
        val stateValidationErrors = mutableListOf<ValidationError>()
        if (aggregatedStates.isNotEmpty()) {
            if (start == null) {
                throw IllegalArgumentException("Workflow should have a start using 'triggered { byEvent or byTime }'")
            }
            treeBuilder.buildTree(startName = start.stateName, aggregatedStates)
            this.withStates(aggregatedStates.toList())

            val operationStates = aggregatedStates.filterIsInstance<OperationState>()
            if (operationStates.isNotEmpty()) {
                this.withFunctions(Functions(
                    operationStates.flatMap { state ->
                        OperationStateBuilder.buildFunctionDefinition(state)
                    }.distinctBy { it.name }
                ))
            }

            val eventStates = aggregatedStates.filterIsInstance<EventState>()
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
        logger.trace("Generate ${workflow.toJsonString()}")
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