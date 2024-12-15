package com.github.frtu.kotlin.test.containers.ollama

import io.kotest.matchers.shouldBe
import java.net.Socket
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.testcontainers.junit.jupiter.Testcontainers

@Disabled
@Testcontainers
class OllamaContainerTest {
    @Test
    fun `Simple test OllamaContainer port open`() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        val ollamaContainer = OllamaContainer()
        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        ollamaContainer.start()
        logger.debug("Started Ollama at port ${ollamaContainer.mappedPortTemporal}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        serverListening("localhost", ollamaContainer.mappedPortTemporal) shouldBe true
        ollamaContainer.stop()
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