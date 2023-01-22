package com.github.frtu.kotlin.test.containers.temporalite

import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

/**
 * Test containers for Temporalite
 */
class TemporaliteContainer(dockerImageName: DockerImageName) :
    GenericContainer<TemporaliteContainer>(dockerImageName) {
    constructor(tag: String = DEFAULT_TAG) : this(DEFAULT_IMAGE_NAME.withTag(tag))

    init {
        dockerImageName.assertCompatibleWith(*arrayOf(DEFAULT_IMAGE_NAME))
        withExposedPorts(7233, 8233)
    }

    companion object {
        private val DEFAULT_IMAGE_NAME = DockerImageName.parse("slamdev/temporalite")
        private const val DEFAULT_TAG = "0.3.0"
    }
}
