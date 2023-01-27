package com.github.frtu.sample.temporal.dynamicwkf.activity

import io.temporal.activity.ActivityInterface
import io.temporal.activity.ActivityMethod
import io.serverlessworkflow.api.Workflow as ServerlessWorkflow

@ActivityInterface
interface ServerlessWorkflowActivity {
    @ActivityMethod
    fun findWorkflow(serverlessWorkflowId: String, serverlessWorkflowVersion: String): ServerlessWorkflow
}

const val TASK_QUEUE_REGISTRY = "TASK_QUEUE_REGISTRY"
