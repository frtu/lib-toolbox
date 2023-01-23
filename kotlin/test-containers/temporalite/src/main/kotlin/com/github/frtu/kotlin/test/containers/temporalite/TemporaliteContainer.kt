package com.github.frtu.kotlin.test.containers.temporalite

import io.temporal.client.WorkflowClient
import io.temporal.serviceclient.WorkflowServiceStubs
import io.temporal.serviceclient.WorkflowServiceStubsOptions
import org.slf4j.LoggerFactory
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.utility.DockerImageName

/**
 * Test containers for Temporalite
 */
class TemporaliteContainer(dockerImageName: DockerImageName) :
    GenericContainer<TemporaliteContainer>(dockerImageName) {
    constructor(tag: String = DEFAULT_TAG) : this(DEFAULT_IMAGE_NAME.withTag(tag))

    init {
        dockerImageName.assertCompatibleWith(*arrayOf(DEFAULT_IMAGE_NAME))
        withExposedPorts(TEMPORAL_PORT, ADMIN_UI_PORT)
        withLogConsumer(Slf4jLogConsumer(logger));
    }

    val mappedPortTemporal: Int
        get() = getMappedPort(TEMPORAL_PORT)

    fun buildWorkflowClient(): WorkflowClient =
        WorkflowClient.newInstance(WorkflowServiceStubs.newServiceStubs(WorkflowServiceStubsOptions {
            val targetEndpoint = "localhost:${mappedPortTemporal}"
            logger.debug("Creating client to $targetEndpoint")
            setTarget(targetEndpoint)
            setEnableHttps(false)
        }))

    companion object {
        private val DEFAULT_IMAGE_NAME = DockerImageName.parse("slamdev/temporalite")
        const val DEFAULT_TAG = "0.3.0"

        const val TEMPORAL_PORT = 7233
        const val ADMIN_UI_PORT = 8233

        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}
