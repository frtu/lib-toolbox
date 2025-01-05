package sample

import io.temporal.client.WorkflowClient
import io.temporal.serviceclient.WorkflowServiceStubs
import io.temporal.worker.WorkerFactory
import sample.activity.AccountActivityImpl
import sample.workflow.MoneyTransferWorkflow
import sample.workflow.MoneyTransferWorkflowImpl

fun main() {
    // WorkflowServiceStubs is a gRPC stubs wrapper that talks to the local Docker instance of the Temporal server.
    val service = WorkflowServiceStubs.newInstance()
    val client = WorkflowClient.newInstance(service)

    // Worker factory is used to create Workers that poll specific Task Queues.
    val factory = WorkerFactory.newInstance(client)
    val worker = factory.newWorker(MoneyTransferWorkflow.TASK_QUEUE)
    // This Worker hosts both Workflow and Activity implementations.
    // Workflows are stateful so a type is needed to create instances.
    worker.registerWorkflowImplementationTypes(MoneyTransferWorkflowImpl::class.java)
    // Activities are stateless and thread safe so a shared instance is used.
    worker.registerActivitiesImplementations(AccountActivityImpl())
    // Start listening to the Task Queue.
    factory.start()

    println("Started Worker for ${MoneyTransferWorkflowImpl::class.java}")
}