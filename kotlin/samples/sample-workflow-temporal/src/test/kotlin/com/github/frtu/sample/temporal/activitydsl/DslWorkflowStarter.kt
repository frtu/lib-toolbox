package com.github.frtu.sample.temporal.activitydsl

import com.fasterxml.jackson.databind.JsonNode
import com.github.frtu.sample.temporal.dynamicwkf.TASK_QUEUE_DSL
import com.github.frtu.sample.temporal.dynamicwkf.serverless.*
import com.github.frtu.sample.temporal.dynamicwkf.utils.IoHelper
import com.github.frtu.sample.temporal.dynamicwkf.utils.IoHelper.getSampleWorkflowInput
import io.temporal.client.WorkflowClient
import io.temporal.serviceclient.WorkflowServiceStubs
import kotlin.system.exitProcess

fun main() {
    val service = WorkflowServiceStubs.newInstance()
    val client = WorkflowClient.newInstance(service)

    val workflowIdToDataInputMap: Map<String, String> = mapOf(
        "customerapplication" to "dsl/customerapplication/datainput.json",
    )
    ServerlessWorkflowRegistry.register(IoHelper.getFileAsString("dsl/customerapplication/workflow.yml"))

    for ((workflowId, dataInputFileName) in workflowIdToDataInputMap) {
        dslWorkflow(client, workflowId, "1.0", dataInputFileName)
    }
    exitProcess(0)
}

private fun dslWorkflow(
    client: WorkflowClient,
    workflowId: String,
    workflowVersion: String,
    dataInputFileName: String,
) {
    try {
        // Get the workflow dsl from cache
        val dslWorkflow = ServerlessWorkflowRegistry.get(workflowId, workflowVersion)
            .assertValidity()

        val workflowStub = client.newUntypedWorkflowStub(
            dslWorkflow.name,
            dslWorkflow.toWorkflowOptions(TASK_QUEUE_DSL)
        )
        println(
            "Starting workflow with id: $workflowId and version: $workflowVersion"
        )

        // Start workflow execution
        dslWorkflow.start(workflowStub, getSampleWorkflowInput(dataInputFileName))
        // Wait for workflow to finish
        val result = workflowStub.getResult(JsonNode::class.java)

        // Query the customer name and age
        val customerName = workflowStub.query("QueryCustomerName", String::class.java)
        val customerAge = workflowStub.query("QueryCustomerAge", Int::class.java)
        println("Query result for customer name: $customerName")
        println("Query result for customer age: $customerAge")

        // Print workflow results
        println("Workflow results:${result.toPrettyString()}".trimIndent())
    } catch (e: ServerlessValidationException) {
        System.err.println(
            "Workflow DSL not valid. Consult github.com/serverlessworkflow/specification/blob/main/specification.md for more info"
        )
        for (error in e.errors) {
            println("Error: " + error.message)
        }
        exitProcess(1)
    } catch (e: Exception) {
        e.printStackTrace()
        System.err.println("Error: " + e.message)
    }
}