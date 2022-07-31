package com.github.frtu.sample.source.sync

import com.github.frtu.logs.core.RpcLogger.*
import com.github.frtu.sample.domain.EmailCrudHandler
import com.github.frtu.sample.persistence.basic.EmailEntity
import com.github.frtu.sample.temporal.staticwkf.SubscriptionEvent
import com.github.frtu.sample.temporal.staticwkf.starter.SubscriptionHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.*
import java.net.URI

@Configuration
class RouterConfig(
    private val subscriptionHandler: SubscriptionHandler,
    private val emailCrudHandler: EmailCrudHandler,
) {
    @Bean
    fun route(): RouterFunction<*> = coRouter {
        // Simple CRUD
        val uriPathEmails = "/v1/emails"
        GET(uriPathEmails) {
            rpcLogger.info(uri(uriPathEmails), message("query all"))
            ok().json().bodyAndAwait(emailCrudHandler.queryMany())
        }
        GET("/v1/emails/{id}") { serverRequest ->
            val id = serverRequest.pathVariable("id")
            val entity = emailCrudHandler.queryOne(id)
            when {
                entity != null -> ok().json().bodyValueAndAwait(entity)
                else -> notFound().buildAndAwait()
            }
        }
        POST(uriPathEmails) { serverRequest ->
            val emailEntity = serverRequest.awaitBody<EmailEntity>()
            val createdId = emailCrudHandler.insertOne(emailEntity)
            rpcLogger.info(uri(uriPathEmails), requestBody(emailEntity), responseBody(createdId))
            created(URI.create("$uriPathEmails/$createdId")).buildAndAwait()
        }
    }

    internal val rpcLogger = create(this::class.java)
}