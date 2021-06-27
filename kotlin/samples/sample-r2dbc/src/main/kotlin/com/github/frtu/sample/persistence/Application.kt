package com.github.frtu.sample.persistence

import com.github.frtu.persistence.r2dbc.config.PostgresR2dbcConfiguration
import com.github.frtu.sample.persistence.r2dbc.basic.Email
import com.github.frtu.sample.persistence.r2dbc.basic.IEmailRepository
import io.r2dbc.spi.ConnectionFactory
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.core.io.ClassPathResource
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator

@SpringBootApplication
@Import(PostgresR2dbcConfiguration::class)
class Application {
    @Bean
    fun initializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer {
        val initializer = ConnectionFactoryInitializer()
        initializer.setConnectionFactory(connectionFactory)
        val populator = CompositeDatabasePopulator()
        populator.addPopulators(ResourceDatabasePopulator(ClassPathResource("./db/migration/V0_1_0__h2-table-email.sql")))
        initializer.setDatabasePopulator(populator)
        return initializer
    }

    @Bean
    fun initDatabase(repository: IEmailRepository): CommandLineRunner {
        return CommandLineRunner {
            val entity = Email(
                "rndfred@gmail.com", "Mail subject",
                "Lorem ipsum dolor sit amet.", "SENT"
            )
            runBlocking {
                repository.save(entity)
                logger.debug(repository.findAll().toList(mutableListOf()).toString())
            }
        }
    }

    internal val logger = LoggerFactory.getLogger(this::class.java)
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
