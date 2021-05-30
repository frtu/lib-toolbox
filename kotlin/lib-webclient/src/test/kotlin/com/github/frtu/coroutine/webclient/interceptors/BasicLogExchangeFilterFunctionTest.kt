package com.github.frtu.coroutine.webclient.interceptors

import com.github.frtu.coroutine.webclient.BaseMockWebServerTest
import com.github.frtu.coroutine.webclient.SuspendableWebClient
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.web.reactive.function.client.WebClient
import java.util.*

@ExtendWith(MockKExtension::class)
class BasicLogExchangeFilterFunctionTest : BaseMockWebServerTest() {
    private fun suspendableWebClient(): SuspendableWebClient {
        val webClient = WebClient.builder()
            .baseUrl("http://localhost:${mockWebServer.port}")
            .filter(BasicLogExchangeFilterFunction())
            .build()
        return SuspendableWebClient(webClient)
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
        Assertions.assertThat(toList.size).isEqualTo(1)
        Assertions.assertThat(toList[0]).isEqualTo(responseBody)
    }
}