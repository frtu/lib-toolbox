package com.github.frtu.sample.serverless.workflow

import io.serverlessworkflow.api.functions.FunctionDefinition
import io.serverlessworkflow.api.interfaces.State
import io.serverlessworkflow.utils.WorkflowUtils
import io.serverlessworkflow.api.Workflow as ServerlessWorkflow

/**
 * Facilitator for all io.serverlessworkflow.api.Workflow usage.
 */
fun ServerlessWorkflow.toJson(): String = ServerlessWorkflow.toJson(this)
fun String.toServerlessWorkflow(): ServerlessWorkflow = ServerlessWorkflow.fromSource(this)

fun ServerlessWorkflow.getStartingState(): State? = WorkflowUtils.getStartingState(this)
fun ServerlessWorkflow.getStateWithName(nextStateName: String): State? =
    WorkflowUtils.getStateWithName(this, nextStateName)

fun ServerlessWorkflow.getFunctionDefinitionWithName(name: String): FunctionDefinition? =
    if (!WorkflowUtils.hasFunctionDefs(this)) null
    else this.functions.functionDefs.firstOrNull { fd -> fd.name.equals(name) }

fun ServerlessWorkflow.getFunctionDefinitionsWithType(type: FunctionDefinition.Type): List<FunctionDefinition> =
    if (!WorkflowUtils.hasFunctionDefs(this)) emptyList()
    else this.functions.functionDefs.filter { fd -> fd.type.equals(type) }

fun ServerlessWorkflow.getFunctionDefinitionsWithTypeExpression(): List<FunctionDefinition> =
    getFunctionDefinitionsWithType(FunctionDefinition.Type.EXPRESSION)

fun ServerlessWorkflow.getFunctionDefinitionsWithTypeActivity(): List<FunctionDefinition> =
    getFunctionDefinitionsWithType(FunctionDefinition.Type.CUSTOM).filter {
        it.metadata["activity"]?.isNotBlank() ?: false
    }
