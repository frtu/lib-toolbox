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
open class TemporaliteContainer(
    dockerImageName: DockerImageName,
    private val endpoint: String? = null,
) : GenericContainer<TemporaliteContainer>(dockerImageName) {
    constructor(tag: String = DEFAULT_TAG, endpoint: String? = null) : this(DEFAULT_IMAGE_NAME.withTag(tag), endpoint)

    init {
        dockerImageName.assertCompatibleWith(*arrayOf(DEFAULT_IMAGE_NAME))
        withExposedPorts(TEMPORAL_PORT, ADMIN_UI_PORT)
        withLogConsumer(Slf4jLogConsumer(logger));
    }

    fun isUsingTestContainer() = endpoint.isNullOrBlank() || !endpoint.contains(":")

    override fun start() = if (isUsingTestContainer()) {
        logger.info("Starting container $containerId")
        super.start()
    } else {
        logger.info("Preparing to connect to $endpoint")
    }

    val mappedPortTemporal: Int
        get() = if (isUsingTestContainer()) {
            getMappedPort(TEMPORAL_PORT)
        } else {
            throw IllegalStateException("Cannot call mappedPortTemporal if endpoint=$endpoint configured!")
        }

    fun buildWorkflowClient(): WorkflowClient {
        val targetEndpoint = if (isUsingTestContainer()) {
            "localhost:${mappedPortTemporal}"
        } else {
            endpoint
        }
        val enableHttps = false
        return WorkflowClient.newInstance(WorkflowServiceStubs.newServiceStubs(WorkflowServiceStubsOptions {
            logger.debug("Creating client to $targetEndpoint")
            setTarget(targetEndpoint)
            setEnableHttps(enableHttps)
        }))
    }

    companion object {
        private val DEFAULT_IMAGE_NAME = DockerImageName.parse("slamdev/temporalite")
        const val DEFAULT_TAG = "0.10.7"

        const val TEMPORAL_PORT = 7233
        const val ADMIN_UI_PORT = 8233

        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}
