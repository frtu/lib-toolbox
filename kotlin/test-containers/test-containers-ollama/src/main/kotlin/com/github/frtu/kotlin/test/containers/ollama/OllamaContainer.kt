package com.github.frtu.kotlin.test.containers.ollama

import io.temporal.client.WorkflowClient
import io.temporal.serviceclient.WorkflowServiceStubs
import io.temporal.serviceclient.WorkflowServiceStubsOptions
import org.slf4j.LoggerFactory
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.utility.DockerImageName

/**
 * Test containers for Ollama
 * @see <a href="https://ollama.com/blog/ollama-is-now-available-as-an-official-docker-image">Ollama docker image</a>
 */
open class OllamaContainer(
    dockerImageName: DockerImageName,
    private val endpoint: String? = null,
) : GenericContainer<OllamaContainer>(dockerImageName) {
    constructor(tag: String = DEFAULT_TAG, endpoint: String? = null) : this(DEFAULT_IMAGE_NAME.withTag(tag), endpoint)

    init {
        dockerImageName.assertCompatibleWith(*arrayOf(DEFAULT_IMAGE_NAME))
        withExposedPorts(API_PORT, API_PORT)
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
            getMappedPort(API_PORT)
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
        // https://hub.docker.com/r/ollama/ollama/tags
        private val DEFAULT_IMAGE_NAME = DockerImageName.parse("ollama/ollama")
        const val DEFAULT_TAG = "0.5.2"

        const val API_PORT = 11434

        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}
