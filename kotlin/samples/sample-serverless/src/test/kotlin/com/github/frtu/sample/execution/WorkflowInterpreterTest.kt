package com.github.frtu.sample.execution

import com.github.frtu.kotlin.utils.io.toJsonNode
import com.github.frtu.sample.TestResourceLoader
import com.github.frtu.sample.serverless.workflow.ServerlessWorkflowParser
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory

@DisplayName("WorkflowInterpreter tests")
@ExtendWith(MockKExtension::class)
internal class WorkflowInterpreterTest {
    @Test
    fun `parsing DSL - CORRECT`() {
        // --------------------------------------
        // 1. Init
        // --------------------------------------
        val serverlessWorkflow = ServerlessWorkflowParser.parse(
            TestResourceLoader.loadWorkflowDSLSwitch()
        )
        val dataNode = TestResourceLoader.loadData().toJsonNode()["greetings"]

        // --------------------------------------
        // 2. Execution
        // --------------------------------------
        val workflowInterpreter = WorkflowInterpreter(serverlessWorkflow)
        workflowInterpreter.start(dataNode)
//        logger.debug("result:{}", result.toJson())

        // --------------------------------------
        // 3. Validate
        // --------------------------------------
//        result.shouldNotBeNull()
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}