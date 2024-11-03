package com.github.frtu.kotlin.tool.execution.durable

import com.github.frtu.kotlin.serdes.json.ext.objToJsonNode
import com.github.frtu.kotlin.test.containers.temporalite.TemporaliteContainer
import com.github.frtu.kotlin.tool.execution.durable.temporal.activity.ToolAsActivityImpl
import com.github.frtu.kotlin.tool.execution.durable.temporal.workflow.BasicToolWorkflowImpl
import com.github.frtu.kotlin.tool.execution.durable.temporal.workflow.ToolWorkflow
import com.github.frtu.kotlin.tool.execution.durable.temporal.stub.WorkflowCallAsTool
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.temporal.client.WorkflowClient
import io.temporal.worker.WorkerFactory
import java.util.UUID
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.testcontainers.junit.jupiter.Testcontainers
import sample.model.TransferInfo
import sample.tool.IdentityTool

@Testcontainers
internal class DurableToolExecutionTest : TemporaliteContainer(endpoint = "localhost:7233") {
    private lateinit var client: WorkflowClient

    @Test
    fun `Calling workflow as a Tool`(): Unit = runBlocking {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        // Init internal activity tool
        val tool = IdentityTool()
        val toolAsActivityImpl = ToolAsActivityImpl(tool)

        // Init workflow
        val workflowId = ToolWorkflow::class.java.simpleName // Registered Workflow name in the [Worker]
        val taskQueue = BasicToolWorkflowImpl.TASK_QUEUE // Task Queue from the Workflow
        // BasicToolWorkflowImpl is reusing the same input output
        val parameterClass = TransferInfo::class.java // Specific Input from method
        val returnClass = TransferInfo::class.java // Specific Output from method

        // Init execution
        val transferInfo = TransferInfo(
            referenceId = UUID.randomUUID().toString(),
            fromAccountId = "001-001",
            toAccountId = "002-002",
            amount = 18.74,
        )
        // Init service
        // Worker factory is used to create Workers that poll specific Task Queues.
        val factory = WorkerFactory.newInstance(client)
        val worker = factory.newWorker(taskQueue)
        // This Worker hosts both Workflow and Activity implementations.
        // Workflows are stateful so a type is needed to create instances.
        worker.registerWorkflowImplementationTypes(BasicToolWorkflowImpl::class.java)
        // Activities are stateless and thread safe so a shared instance is used.
        worker.registerActivitiesImplementations(
            toolAsActivityImpl,
        )
        // Start listening to the Task Queue.
        factory.start()

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val workflowCallAsTool = WorkflowCallAsTool(
            workflowId = workflowId,
            description = "Calling Workflow as a Tool",
            parameterClass = parameterClass,
            returnClass = returnClass,
            workflowClient = client,
            taskQueue = taskQueue,
        )

        val result = workflowCallAsTool.execute(transferInfo.objToJsonNode())
        logger.info("result:$result")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        with(result) {
            shouldNotBeNull()
            this["referenceId"].textValue() shouldBe transferInfo.referenceId
            this["fromAccountId"].textValue() shouldBe transferInfo.fromAccountId
            this["toAccountId"].textValue() shouldBe transferInfo.toAccountId
            this["amount"].doubleValue() shouldBe transferInfo.amount
        }
    }

    @BeforeAll
    fun setup() {
        start()
        client = buildWorkflowClient()
    }

    @AfterAll
    fun destroy() {
        stop()
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}
