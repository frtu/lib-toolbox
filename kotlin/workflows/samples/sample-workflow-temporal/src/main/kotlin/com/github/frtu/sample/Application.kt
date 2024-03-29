package com.github.frtu.sample

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.frtu.sample.persistence.basic.IEmailRepository
import com.github.frtu.sample.temporal.staticwkf.SubscriptionEvent
import com.github.frtu.sample.temporal.staticwkf.starter.SubscriptionHandler
import io.r2dbc.spi.ConnectionFactory
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator

@SpringBootApplication
class Application {
    @Bean
    fun initializer(
        coroutineRepository: IEmailRepository,
        subscriptionHandler: SubscriptionHandler,
    ): CommandLineRunner = CommandLineRunner {
        logger.debug("======================================")
        val subscriptionEvent = SubscriptionEvent("""{"key":"value"}""", "event.type")
        logger.debug(objectMapper.writeValueAsString(subscriptionEvent))
        subscriptionHandler.handle(subscriptionEvent)
        logger.debug("======================================")
        val subscriptionEventError = SubscriptionEvent("""{"key":"ERROR"}""", "event.type")
        logger.debug(objectMapper.writeValueAsString(subscriptionEventError))
        subscriptionHandler.handle(subscriptionEventError)
    }

    @Bean
    fun databaseInitializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer =
        ConnectionFactoryInitializer().apply {
            setConnectionFactory(connectionFactory)
            setDatabasePopulator(
                CompositeDatabasePopulator().apply {
                    addPopulators(ResourceDatabasePopulator(ClassPathResource("db/migration/V0_1_0__h2-table-email.sql")))
                }
            )
        }

    private val objectMapper: ObjectMapper =
        jacksonObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)

    internal val logger = LoggerFactory.getLogger(this::class.java)
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
