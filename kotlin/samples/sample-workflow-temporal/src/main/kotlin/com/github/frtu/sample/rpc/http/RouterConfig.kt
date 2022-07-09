package com.github.frtu.sample.rpc.http

import com.github.frtu.logs.core.RpcLogger.*
import com.github.frtu.sample.domain.EmailHandler
import com.github.frtu.sample.persistence.basic.EmailEntity
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.*
import java.net.URI

@Configuration
class RouterConfig(
    private val emailHandler: EmailHandler,
) {
    @Bean
    fun route(): RouterFunction<*> = coRouter {
        val uriPath = "/v1/emails"
        GET(uriPath) { serverRequest ->
            rpcLogger.info(uri(uriPath), message("query all"))
            ok().json().bodyAndAwait(emailHandler.queryMany())
        }
        GET("/v1/emails/{id}") { serverRequest ->
            val id = serverRequest.pathVariable("id")
            val entity = emailHandler.queryOne(id)
            when {
                entity != null -> ok().json().bodyValueAndAwait(entity)
                else -> notFound().buildAndAwait()
            }
        }
        POST(uriPath) { serverRequest ->
            val emailEntity = serverRequest.awaitBody<EmailEntity>()
            val createdId = emailHandler.insertOne(emailEntity)
            rpcLogger.info(uri(uriPath), requestBody(emailEntity), responseBody(createdId))
            created(URI.create("$uriPath/$createdId")).buildAndAwait()
        }
    }

    internal val rpcLogger = create(this::class.java)
}
