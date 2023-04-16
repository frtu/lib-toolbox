package com.github.frtu.sample.temporal.dynamicwkf.serverless

import io.serverlessworkflow.api.Workflow as ServerlessWorkflow

object ServerlessWorkflowRegistry {
    fun register(definition: String): ServerlessWorkflow {
        val serverlessWorkflow = ServerlessWorkflow.fromSource(definition)
        serverlessWorkflowMap[key(serverlessWorkflow.id, serverlessWorkflow.version)] = serverlessWorkflow
        return serverlessWorkflow
    }

    fun get(serverlessWorkflowId: String, serverlessWorkflowVersion: String): ServerlessWorkflow {
        val key = key(serverlessWorkflowId, serverlessWorkflowVersion)
        return serverlessWorkflowMap[key] ?: throw IllegalArgumentException("Workflow $key not registered!!")
    }

    private fun key(serverlessWorkflowId: String, serverlessWorkflowVersion: String) =
        "$serverlessWorkflowId-$serverlessWorkflowVersion"

    private val serverlessWorkflowMap: MutableMap<String, ServerlessWorkflow> = mutableMapOf()
}
