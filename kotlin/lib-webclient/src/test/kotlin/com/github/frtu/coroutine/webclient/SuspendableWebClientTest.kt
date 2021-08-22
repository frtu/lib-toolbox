package com.github.frtu.coroutine.webclient

import io.mockk.junit5.MockKExtension
import io.netty.handler.codec.http.HttpResponseStatus
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactor.asFlux
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*

@ExtendWith(MockKExtension::class)
class SuspendableWebClientTest : BaseMockWebServerTest() {
    private fun suspendableWebClient(): SuspendableWebClient {
        val webClient = WebClient.create("http://localhost:${mockWebServer.port}")
        val suspendableWebClient = SuspendableWebClient(webClient)
        return suspendableWebClient
    }

    @Test
    fun `Base get call`() {
        //--------------------------------------
        // 1. Prepare server data & Init client
        //--------------------------------------
        val responseBody = """{"message":"response"}"""
        mockWebServer.enqueue(
            MockResponse()
                .setBody(responseBody)
                .addHeader("Content-Type", "application/json")
        )

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val toList = runBlocking {
            val flow = suspendableWebClient().get(
                url = "/resources/1234",
                requestId = UUID.randomUUID()
            )
            flow.toList(mutableListOf())
        }
        logger.debug(toList.toString())
        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        assertThat(toList.size).isEqualTo(1)
        assertThat(toList[0]).isEqualTo(responseBody)
    }


    @Test
    fun `Resilient get call`() {
        runBlocking {
            //--------------------------------------
            // 1. Prepare server data & Init client
            //--------------------------------------
            // Simulate 2 errors
            for (index in 1..2) {
                mockWebServer.enqueue(MockResponse().setResponseCode(HttpResponseStatus.SERVICE_UNAVAILABLE.code()))
            }
            // Before returning the correct response
            val responseBody = """{"message":"response"}"""
            mockWebServer.enqueue(
                MockResponse()
                    .setBody(responseBody)
                    .addHeader("Content-Type", "application/json")
            )

            //--------------------------------------
            // 2. Execute
            //--------------------------------------
            StepVerifier
                .create(
                    suspendableWebClient().get(
                        url = "/resources/1234",
                        requestId = UUID.randomUUID()
                    ).asFlux()
                )
                .expectNextMatches { response -> response.equals("""{"message":"response"}""") }
                .verifyComplete()
        }
    }

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

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        var webClientResponse: WebClientResponse? = null
        runBlocking {
            suspendableWebClient().post(
                url = "/resources/1234",
                requestId = UUID.randomUUID(),
                requestBody = """{"message":"request"}""",
                responseCallback = { result ->
                    webClientResponse = result
                }
            )
        }
        webClientResponse
            ?.let { logger.debug("statusCode=${it.statusCode} reponseBody=${it.reponseBody}") }
            ?: run { logger.error("webClientResponse is empty!") }
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
        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        assertThrows<WebClientResponseException>("This should throw an illegal argument exception") {
            runBlocking {
                suspendableWebClient().post(
                    url = "/resources/1234",
                    requestId = UUID.randomUUID(),
                    requestBody = """{"message":"request"}""",
                )
            }
        }
    }

    @Test
    fun `Async post call`() {
        //--------------------------------------
        // 1. Prepare server data & Init client
        //--------------------------------------
        val responseBody = """{"message":"response"}"""
        mockWebServer.enqueue(
            MockResponse()
                .setBody(responseBody)
                .addHeader("Content-Type", "application/json")
        )

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        var webClientResponse: WebClientResponse? = null
        runBlocking {
            suspendableWebClient().post(
                url = "/resources/1234",
                requestId = UUID.randomUUID(),
                publisher = Mono.just("""{"message":"request"}"""),
                elementClass = String::class.java,
                responseCallback = { result ->
                    webClientResponse = result
                }
            )
        }

        webClientResponse
            ?.let { logger.debug("statusCode=${it.statusCode} reponseBody=${it.reponseBody}") }
            ?: run { logger.error("webClientResponse is empty!") }
        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        assertThat(webClientResponse).isNotNull
        assertThat(webClientResponse?.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(webClientResponse?.reponseBody).isEqualTo(responseBody)
    }
}