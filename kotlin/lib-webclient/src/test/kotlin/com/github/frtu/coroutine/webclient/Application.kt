package com.github.frtu.coroutine.webclient

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.router
import javax.annotation.PreDestroy

@Configuration
class WebConfig {
    @Bean
    fun route(): RouterFunction<*> = router {
        GET("/", { r ->
            ok().body(
                BodyInserters.fromValue("""{"message":"response"}""")
            )
        })
    }
}


@SpringBootApplication
class Application {
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

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}