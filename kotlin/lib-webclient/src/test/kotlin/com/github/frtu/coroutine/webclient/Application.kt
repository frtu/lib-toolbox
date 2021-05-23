package com.github.frtu.coroutine.webclient

import com.github.frtu.coroutine.webclient.config.WebClientBuilder
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.coRouter
import org.springframework.web.reactive.function.server.json
import javax.annotation.PreDestroy

@Configuration
class WebClientConfig {
    @Bean
    fun suspendableWebClient(mockWebServer: MockWebServer): SuspendableWebClient =
        SuspendableWebClient.create("http://localhost:${mockWebServer.port}")

    @Bean
    fun route(suspendableWebClient: SuspendableWebClient): RouterFunction<*> = coRouter {
        GET("/", { r ->
            ok().json().bodyAndAwait(suspendableWebClient.get("/"))
        })
    }
}

@Configuration
class MockServerConfig {
    lateinit var mockWebServer: MockWebServer

    @Bean
    fun mockWebServer(): MockWebServer {
        mockWebServer = MockWebServer()
        logger.debug("=== ${this.javaClass.simpleName} starting mockWebServer:${mockWebServer}")

        mockWebServer.start()

        mockWebServer.enqueue(
            MockResponse()
                .setBody("""{"message":"response"}""")
                .addHeader("Content-Type", "application/json")
        )
        return mockWebServer
    }

    @PreDestroy
    @Throws(Exception::class)
    fun onDestroy() {
        logger.debug("=== ${this.javaClass.simpleName} stopping mockWebServer")
        mockWebServer.shutdown()
    }

    private val logger = LoggerFactory.getLogger(Application::class.java)
}

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}