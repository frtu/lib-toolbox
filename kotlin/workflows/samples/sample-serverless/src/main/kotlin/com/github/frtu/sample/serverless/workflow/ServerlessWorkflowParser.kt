package com.github.frtu.sample.serverless.workflow

import io.serverlessworkflow.validation.WorkflowValidatorImpl
import io.serverlessworkflow.api.Workflow as ServerlessWorkflow

/**
 * Stateless parser for DSL.
 *
 * MUST be the central location to create & validate all io.serverlessworkflow.api.Workflow instances.
 */
object ServerlessWorkflowParser {

    @Throws(ServerlessValidationException::class)
    fun parse(definition: String): ServerlessWorkflow {
        val serverlessWorkflow = definition.toServerlessWorkflow()
        return assertValidity(serverlessWorkflow)
    }

    private fun assertValidity(serverlessWorkflow: ServerlessWorkflow): ServerlessWorkflow {
        val workflowValidator = WorkflowValidatorImpl().setWorkflow(serverlessWorkflow)

        if (!workflowValidator.isValid) {
            val validationErrorList = workflowValidator.validate()
            throw ServerlessValidationException(validationErrorList)
        }
        return serverlessWorkflow
    }
}
