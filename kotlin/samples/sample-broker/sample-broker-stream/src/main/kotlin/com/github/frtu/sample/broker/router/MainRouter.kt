package com.github.frtu.sample.broker.router

import com.github.frtu.logs.core.RpcLogger
import com.github.frtu.logs.core.RpcLogger.requestBody
import com.github.frtu.logs.webflux.SpringLoggerHelper.serverRequest
import com.github.frtu.sample.broker.service.MessageProducerService
import com.github.frtu.sample.persistence.r2dbc.basic.Email
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.buildAndAwait
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class MainRouter constructor(private val messageProducerService: MessageProducerService) {
    @Bean
    fun router() = coRouter {
        "/api/v1".nest {
            POST("produce-message") { req ->
                val payload = req.awaitBody(Email::class)
                rpcLogger.info(serverRequest(req), requestBody(payload))
                messageProducerService.sendMessage(payload)
                ServerResponse.ok().buildAndAwait()
            }
        }
    }

    internal val logger = LoggerFactory.getLogger(this::class.java)
    internal val rpcLogger = RpcLogger.create(logger)
}
