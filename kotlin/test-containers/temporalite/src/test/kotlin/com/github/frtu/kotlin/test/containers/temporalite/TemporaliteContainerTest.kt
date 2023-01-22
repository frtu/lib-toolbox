package com.github.frtu.kotlin.test.containers.temporalite

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.testcontainers.junit.jupiter.Testcontainers
import java.net.Socket

@Testcontainers
class TemporaliteContainerTest {
    @Test
    fun `test`() {
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