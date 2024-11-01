package com.github.frtu.kotlin.llm.os.agent.durable.temporal

import com.github.frtu.kotlin.test.containers.temporalite.TemporaliteContainer
import io.temporal.client.WorkflowClient
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

internal class ActivityCallAsToolTest {
    private val temporaliteContainer = TemporaliteContainer()

    lateinit var client: WorkflowClient

    @Test
    fun test() {
        logger.debug("Started Temporal at port ${temporaliteContainer.mappedPortTemporal}")
        client = temporaliteContainer.buildWorkflowClient()
    }

    @BeforeAll
    fun setup() {
        client.options
    }

    @AfterAll
    fun close() {
        temporaliteContainer.stop()
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}
