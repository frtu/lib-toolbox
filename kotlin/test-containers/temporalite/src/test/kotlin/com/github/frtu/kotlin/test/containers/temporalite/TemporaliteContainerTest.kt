package com.github.frtu.kotlin.test.containers.temporalite

import com.github.frtu.kotlin.test.containers.temporalite.workflow.AccountActivityImpl
import com.github.frtu.kotlin.test.containers.temporalite.workflow.MoneyTransferWorkflow
import com.github.frtu.kotlin.test.containers.temporalite.workflow.MoneyTransferWorkflowImpl
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank
import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowOptions
import io.temporal.worker.WorkerFactory
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.testcontainers.junit.jupiter.Testcontainers
import java.net.Socket
import java.util.*

@Testcontainers
class TemporaliteContainerTest {
    @Test
    fun `Simple test TemporaliteContainer port open`() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        val temporaliteContainer = TemporaliteContainer()
        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        temporaliteContainer.start()
        logger.debug("Started Temporal at port ${temporaliteContainer.mappedPortTemporal}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        serverListening("localhost", temporaliteContainer.mappedPortTemporal) shouldBe true
        temporaliteContainer.stop()
    }

    @Test
    fun `Test TemporaliteContainer workflow registration`() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        val workflowInstanceName = "money-transfer-workflow-${UUID.randomUUID()}"

        val temporaliteContainer = TemporaliteContainer()
        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        temporaliteContainer.start()
        logger.debug("Started Temporal at port ${temporaliteContainer.mappedPortTemporal}")

        val client = temporaliteContainer.buildWorkflowClient()
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

        val options = WorkflowOptions.newBuilder()
            .setTaskQueue(MoneyTransferWorkflow.TASK_QUEUE) // A WorkflowId prevents this it from having duplicate instances, remove it to duplicate.
            .setWorkflowId(workflowInstanceName)
            .build()
        // WorkflowStubs enable calls to methods as if the Workflow object is local, but actually perform an RPC.
        val workflow: MoneyTransferWorkflow = client.newWorkflowStub(MoneyTransferWorkflow::class.java, options)
        val referenceId = UUID.randomUUID().toString()
        val fromAccount = "001-001"
        val toAccount = "002-002"
        val amount = 18.74

        // Asynchronous execution. This process will exit after making this call.
        val we = WorkflowClient.start(workflow::transfer, fromAccount, toAccount, referenceId, amount)
        logger.debug("\nTransfer of $amount from account $fromAccount to account $toAccount is processing\n")
        logger.debug("\nWorkflowID: ${we.workflowId} RunID: ${we.runId}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        we.workflowId shouldBe workflowInstanceName
        we.runId.shouldNotBeBlank()
        temporaliteContainer.stop()
    }

    fun serverListening(host: String, port: Int): Boolean {
        var s: Socket? = null
        return try {
            s = Socket(host, port)
            true
        } catch (e: Exception) {
            false
        } finally {
            if (s != null) try {
                s.close()
            } catch (e: Exception) {
            }
        }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}