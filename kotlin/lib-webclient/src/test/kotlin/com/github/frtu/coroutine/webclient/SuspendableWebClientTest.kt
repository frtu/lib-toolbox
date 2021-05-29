package com.github.frtu.coroutine.webclient

import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import java.util.*

@ExtendWith(MockKExtension::class)
class SuspendableWebClientTest : BaseMockWebServerTest() {
    @Test
    fun `Base post call`() {
        //--------------------------------------
        // 1. Prepare server data & Init client
        //--------------------------------------
        val responseBody = """{"message":"response"}"""
        mockWebServer.enqueue(
            MockResponse()
                .setBody(responseBody)
                .addHeader("Content-Type", "application/json")
        )

        val webClient = WebClient.create("http://localhost:${mockWebServer.port}")
        val suspendableWebClient = SuspendableWebClient(webClient)

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        var webClientResponse: WebClientResponse? = null
        runBlocking {
            suspendableWebClient.post(
                url = "/resources/1234",
                requestId = UUID.randomUUID(),
                requestBody = """{"message":"request"}""",
                responseCallback = { result ->
                    webClientResponse = result
                }
            )
        }
        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        assertThat(webClientResponse).isNotNull
        assertThat(webClientResponse?.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(webClientResponse?.reponseBody).isEqualTo(responseBody)
    }

    @Test
    fun `Post call 500 error`() {
        //--------------------------------------
        // 1. Prepare server data & Init client
        //--------------------------------------
        val responseBody = """{"error":"error message"}"""
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setBody(responseBody)
                .addHeader("Content-Type", "application/json")
        )

        val webClient = WebClient.create("http://localhost:${mockWebServer.port}")
        val suspendableWebClient = SuspendableWebClient(webClient)

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        assertThrows<WebClientResponseException>("This should throw an illegal argument exception") {
            runBlocking {
                suspendableWebClient.post(
                    url = "/resources/1234",
                    requestId = UUID.randomUUID(),
                    requestBody = """{"message":"request"}""",
                )
            }
        }
    }
}