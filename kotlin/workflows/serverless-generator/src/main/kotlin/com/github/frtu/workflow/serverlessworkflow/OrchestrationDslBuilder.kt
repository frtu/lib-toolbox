package com.github.frtu.workflow.serverlessworkflow

import com.github.frtu.workflow.serverlessworkflow.workflow.WorkflowBuilder
import io.serverlessworkflow.api.Workflow as ServerlessWorkflow

/**
 * workflow DSL
 *
 * @author frtu
 * @since 1.2.5
 */
@DslBuilder
fun workflow(name: String? = null, options: WorkflowBuilder.() -> Unit): ServerlessWorkflow {
    val workflowBuilder = WorkflowBuilder(name)
    workflowBuilder.apply(options)
    return workflowBuilder.build()
}
