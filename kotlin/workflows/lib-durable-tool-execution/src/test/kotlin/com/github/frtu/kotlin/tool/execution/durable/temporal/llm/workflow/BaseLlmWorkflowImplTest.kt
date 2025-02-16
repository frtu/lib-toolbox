package com.github.frtu.kotlin.tool.execution.durable.temporal.llm.workflow

import com.github.frtu.kotlin.ai.spring.builder.ChatApiConfigs
import com.github.frtu.kotlin.test.containers.temporalite.TemporaliteContainer
import com.github.frtu.kotlin.tool.execution.durable.temporal.llm.activity.LlmActivityImpl
import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowOptions
import io.temporal.worker.WorkerFactory
import java.util.UUID
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.testcontainers.junit.jupiter.Testcontainers
import sample.tool.workflow.SummariserLlmWorkflowImpl

@Disabled
@Testcontainers
class BaseLlmWorkflowImplTest : TemporaliteContainer(
    // UNCOMMENT to switch to remote temporal service
//    endpoint = "localhost:7233",
) {
    private lateinit var client: WorkflowClient

    private val chat = ChatApiConfigs().chatOllama(
        model = "llama3",
    )

    @Test
    fun `Calling a specialised BaseLlmWorkflowImpl for Summariser`(): Unit = runBlocking {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        val userMessage = "Hey this is fred how are you paul? All good thanks"

        // Init internal activity agent
        val agent = LlmActivityImpl(chat = chat)

        // Init workflow
        val workflowId = "${SummariserLlmWorkflowImpl::class.java.simpleName}-${UUID.randomUUID()}" // Workflow Id should be unique per exec
        val taskQueue = SummariserLlmWorkflowImpl.TASK_QUEUE // Task Queue from the Workflow
        val options = WorkflowOptions.newBuilder()
            .setTaskQueue(taskQueue)
            .setWorkflowId(workflowId)
            .build()

        // Init service
        // Worker factory is used to create Workers that poll specific Task Queues.
        val factory = WorkerFactory.newInstance(client)
        val worker = factory.newWorker(taskQueue)
        // This Worker hosts both Workflow and Activity implementations.
        // Workflows are stateful so a type is needed to create instances.
        worker.registerWorkflowImplementationTypes(SummariserLlmWorkflowImpl::class.java)
        // Activities are stateless and thread safe so a shared instance is used.
        worker.registerActivitiesImplementations(
            agent,
        )
        // Start listening to the Task Queue.
        factory.start()

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val workflow: LlmWorkflow = client.newWorkflowStub(LlmWorkflow::class.java, options)
        val we = WorkflowClient.start(workflow::chat, userMessage)
        logger.info("\nWorkflowID: ${we.workflowId} RunID: ${we.runId}")
//        val result = workflow.chat(userMessage)
//        logger.info("result:$result")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
//        with(result) {
//            shouldNotBeNull()
//        }
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