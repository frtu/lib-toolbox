package com.github.frtu.sample.temporal.dynamicwkf.activity

import com.github.frtu.sample.temporal.dynamicwkf.serverless.ServerlessWorkflowRegistry
import com.github.frtu.workflow.temporal.annotation.ActivityImplementation
import org.springframework.stereotype.Repository
import io.serverlessworkflow.api.Workflow as ServerlessWorkflow

@Repository
@ActivityImplementation(taskQueue = TASK_QUEUE_REGISTRY)
class ServerlessWorkflowActivityImpl : ServerlessWorkflowActivity {
    override fun findWorkflow(serverlessWorkflowId: String, serverlessWorkflowVersion: String): ServerlessWorkflow =
        ServerlessWorkflowRegistry.get(serverlessWorkflowId, serverlessWorkflowVersion)
}
