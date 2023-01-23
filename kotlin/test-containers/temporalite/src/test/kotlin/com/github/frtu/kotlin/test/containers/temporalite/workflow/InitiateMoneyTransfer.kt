package com.github.frtu.kotlin.test.containers.temporalite.workflow

import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowOptions
import io.temporal.serviceclient.WorkflowServiceStubs
import java.util.*
import kotlin.system.exitProcess

fun main() {
    // WorkflowServiceStubs is a gRPC stubs wrapper that talks to the local Docker instance of the Temporal server.
    val service = WorkflowServiceStubs.newInstance()
    val options = WorkflowOptions.newBuilder()
        .setTaskQueue(MoneyTransferWorkflow.TASK_QUEUE) // A WorkflowId prevents this it from having duplicate instances, remove it to duplicate.
        .setWorkflowId("money-transfer-workflow")
        .build()
    // WorkflowClient can be used to start, signal, query, cancel, and terminate Workflows.
    val client = WorkflowClient.newInstance(service)

    // WorkflowStubs enable calls to methods as if the Workflow object is local, but actually perform an RPC.
    val workflow: MoneyTransferWorkflow = client.newWorkflowStub(MoneyTransferWorkflow::class.java, options)
    val referenceId = UUID.randomUUID().toString()
    val fromAccount = "001-001"
    val toAccount = "002-002"
    val amount = 18.74

    // Asynchronous execution. This process will exit after making this call.
    val we = WorkflowClient.start(workflow::transfer, fromAccount, toAccount, referenceId, amount)
    println("\nTransfer of $amount from account $fromAccount to account $toAccount is processing\n")
    println("\nWorkflowID: ${we.workflowId} RunID: ${we.runId}")
    exitProcess(0)
}