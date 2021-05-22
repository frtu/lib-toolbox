package com.github.frtu.coroutine.webclient

import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test

import java.util.*

import okhttp3.mockwebserver.MockResponse

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.web.reactive.function.client.WebClient

@ExtendWith(MockKExtension::class)
class SuspendableWebClientTest: BaseMockWebServerTest() {
    @Test
    fun `Base post call`() {
        val baseUrl = "http://localhost:${mockWebServer.port}"
        val webClient = WebClient.create(baseUrl)
        val suspendableWebClient = SuspendableWebClient(webClient)

        mockWebServer.enqueue(
            MockResponse()
                .setBody("{\"response\":\"message\"}")
                .addHeader("Content-Type", "application/json")
        )

        runBlocking {
            suspendableWebClient.post("/resources/1234", UUID.randomUUID(), "message")
            delay(1000)
        }
    }
}