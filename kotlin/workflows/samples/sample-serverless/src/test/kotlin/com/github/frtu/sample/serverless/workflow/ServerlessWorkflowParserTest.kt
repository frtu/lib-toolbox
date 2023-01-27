package com.github.frtu.sample.serverless.workflow

import com.github.frtu.sample.TestResourceLoader
import io.kotlintest.matchers.string.contain
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory

@DisplayName("ServerlessWorkflowParser tests")
@ExtendWith(MockKExtension::class)
internal class ServerlessWorkflowParserTest {
    @Test
    fun `parsing DSL - CORRECT`() {
        // --------------------------------------
        // 1. Init
        // --------------------------------------
        val definition = TestResourceLoader.loadWorkflowDSLSwitch()

        // --------------------------------------
        // 2. Execution
        // --------------------------------------
        val result = ServerlessWorkflowParser.parse(definition)
        logger.debug("result:{}", result.toJson())

        // --------------------------------------
        // 3. Validate
        // --------------------------------------
        result.shouldNotBeNull()
    }

    @Test
    fun `parsing DSL - INCORRECT`() {
        // --------------------------------------
        // 1. Init
        // --------------------------------------
        val definition = TestResourceLoader.loadWorkflowDSLSwitch(false)

        // --------------------------------------
        // 2. Execution & validate Exception
        // --------------------------------------
        val exception = shouldThrow<ServerlessValidationException> {
            ServerlessWorkflowParser.parse(definition)
        }
        logger.debug("errors:{}", exception.errors)
        exception.errors.size shouldBe 1
        exception.errors[0].message should contain("Operation State action functionRef does not reference an existing workflow function definition")
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}
