package com.github.frtu.workflow.serverlessworkflow

import com.github.frtu.workflow.serverlessworkflow.workflow.WorkflowBuilder
import io.serverlessworkflow.api.Workflow as ServerlessWorkflow

/**
 * workflow DSL
 *
 * @author frtu
 * @since 1.2.5
 */
fun workflow(options: WorkflowBuilder.() -> Unit): ServerlessWorkflow {
    val workflowBuilder = WorkflowBuilder()
    workflowBuilder.apply(options)
    return workflowBuilder.build()
}
