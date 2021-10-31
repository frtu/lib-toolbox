package com.github.frtu.sample.reactive.web.router

import com.github.frtu.logs.core.RpcLogger
import com.github.frtu.logs.core.RpcLogger.*
import com.github.frtu.sample.reactive.web.bridge.Bridge
import com.github.frtu.test.resilience.ChaosGenerator
import kotlinx.coroutines.reactive.asFlow
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_PDF
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Flux
import java.net.URI
import java.util.*

@Configuration
class RouterConfig {
    internal val logger = LoggerFactory.getLogger(this::class.java)
    internal val rpcLogger = RpcLogger.create(logger)
    private val chaosGenerator = ChaosGenerator()

    @Bean
    fun route(bridge: Bridge): RouterFunction<*> = coRouter {
        val uriPath = "/v1/resources"
        GET("/pdf") { serverRequest ->
            ok().contentType(APPLICATION_PDF).bodyAndAwait(bridge.nonBlockingQuery())
        }
        GET("$uriPath/{id}") { serverRequest ->
            val id = serverRequest.pathVariable("id")
            println(id)
            chaosGenerator.raiseException("Error")
            ServerResponse.ok()
                .bodyAndAwait(Flux.just("ok").asFlow())
        }
        POST(uriPath) { serverRequest ->
            val body = serverRequest.awaitBody<String>()
            rpcLogger.info(uri(uriPath), requestBody(body))
            // Create and get ID
            val createdId = UUID.randomUUID()
            rpcLogger.info(uri(uriPath), responseBody(createdId))
            created(URI.create("$uriPath/$createdId")).buildAndAwait()
        }
    }
}
