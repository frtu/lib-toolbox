package com.github.frtu.sample.serverless.workflow

import io.serverlessworkflow.api.functions.FunctionDefinition
import io.serverlessworkflow.api.interfaces.State
import io.serverlessworkflow.utils.WorkflowUtils
import io.serverlessworkflow.validation.WorkflowValidatorImpl
import io.serverlessworkflow.api.Workflow as ServerlessWorkflow

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
