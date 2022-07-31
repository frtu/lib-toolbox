package com.github.frtu.sample.temporal.dynamicwkf.serverless

import com.fasterxml.jackson.databind.JsonNode
import io.serverlessworkflow.api.functions.FunctionDefinition
import io.serverlessworkflow.api.interfaces.State
import io.serverlessworkflow.api.retry.RetryDefinition
import io.serverlessworkflow.api.states.EventState
import io.serverlessworkflow.utils.WorkflowUtils
import io.serverlessworkflow.validation.WorkflowValidatorImpl
import io.temporal.activity.ActivityOptions
import io.temporal.api.common.v1.WorkflowExecution
import io.temporal.client.WorkflowOptions
import io.temporal.client.WorkflowStub
import io.temporal.common.RetryOptions
import java.time.Duration
import io.serverlessworkflow.api.Workflow as ServerlessWorkflow

fun ServerlessWorkflow.start(workflowStub: WorkflowStub, workflowInput: JsonNode): WorkflowExecution {
    val startingDslWorkflowState: State = WorkflowUtils.getStartingState(this)
    return if (startingDslWorkflowState !is EventState) {
        // If no event, start the workflow
        workflowStub.start(this.id, this.version, workflowInput)
    } else {
        val eventState: EventState = startingDslWorkflowState as EventState
        val eventName: String = eventState.onEvents[0].eventRefs[0]
        // send input data as signal data
        workflowStub.signalWithStart(
            eventName, arrayOf<Any>(workflowInput), arrayOf(this.id, this.version)
        )
    }
}

fun ServerlessWorkflow.getState(): State? = WorkflowUtils.getStartingState(this)

fun ServerlessWorkflow.getStateWithName(nextStateName: String): State? =
    WorkflowUtils.getStateWithName(this, nextStateName)

fun ServerlessWorkflow.assertValidity(): ServerlessWorkflow {
    val workflowValidator = WorkflowValidatorImpl().setWorkflow(this)

    if (!workflowValidator.isValid) {
        val validationErrorList = workflowValidator.validate()
        throw ServerlessValidationException(validationErrorList)
    }
    return this
}

fun ServerlessWorkflow.getFunctionDefinitionWithName(name: String): FunctionDefinition? =
    if (!WorkflowUtils.hasFunctionDefs(this)) null
    else
        this.functions.functionDefs.firstOrNull { fd -> fd.name.equals(name) }

fun ServerlessWorkflow.getFunctionDefinitionsWithType(type: FunctionDefinition.Type): List<FunctionDefinition>? =
    if (!WorkflowUtils.hasFunctionDefs(this)) null
    else this.functions.functionDefs.filter { fd -> fd.type.equals(type) }


fun ServerlessWorkflow.toActivityOptions(): ActivityOptions =
    toActivityOptionsBuilder().build()

fun ServerlessWorkflow.toActivityOptionsBuilder(
    backoffCoefficient: Double = 1.0,
): ActivityOptions.Builder {
    val dslActivityOptionsBuilder = ActivityOptions.newBuilder()
    this?.timeouts?.actionExecTimeout?.let {
        dslActivityOptionsBuilder.setStartToCloseTimeout(Duration.parse(it))
    }

    // In SW spec each action (activity) can define a specific retry
    // For this demo we just use the globally defined one for all actions
    this?.retries?.retryDefs?.firstOrNull()?.let {
        val retryDefinition: RetryDefinition = it
        val dslRetryOptionsBuilder = RetryOptions.newBuilder()
        dslRetryOptionsBuilder.setBackoffCoefficient(backoffCoefficient)
        retryDefinition.maxAttempts?.let {
            dslRetryOptionsBuilder.setMaximumAttempts(it.toInt())
        }
        retryDefinition.delay?.let {
            dslRetryOptionsBuilder.setInitialInterval(Duration.parse(it))
        }
        retryDefinition.maxDelay?.let {
            dslRetryOptionsBuilder.setMaximumInterval(Duration.parse(it))
        }
    }
    return dslActivityOptionsBuilder
}

fun ServerlessWorkflow.toWorkflowOptions(taskQueue: String): WorkflowOptions =
    toWorkflowOptionsBuilder().setTaskQueue(taskQueue).build()

fun ServerlessWorkflow.toWorkflowOptionsBuilder(): WorkflowOptions.Builder {
    val dslWorkflowOptionsBuilder = WorkflowOptions.newBuilder()
    this.id?.let {
        dslWorkflowOptionsBuilder.setWorkflowId(it)
    }
    this?.timeouts?.workflowExecTimeout?.duration?.let {
        dslWorkflowOptionsBuilder.setWorkflowExecutionTimeout(Duration.parse(it))
    }
    this?.start?.schedule?.cron?.let {
        dslWorkflowOptionsBuilder.setCronSchedule(it.expression)
    }
    return dslWorkflowOptionsBuilder
}
