package com.github.frtu.sample

import com.github.frtu.sample.persistence.basic.EmailEntity
import com.github.frtu.sample.persistence.basic.IEmailRepository
import io.r2dbc.spi.ConnectionFactory
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
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
    fun initializer(coroutineRepository: IEmailRepository): CommandLineRunner = CommandLineRunner {
        logger.debug("======================================")
        val list = mutableListOf<EmailEntity>()
        runBlocking {
            coroutineRepository.save(
                EmailEntity(
                    "rndfred@gmail.com", "Mail subject",
                    "Lorem ipsum dolor sit amet.", "SENT"
                )
            )
            coroutineRepository.findAll().toList(list)
            logger.debug(list.toString())

            val emailEntity = coroutineRepository.findById(0)
            logger.debug(emailEntity.toString())
        }
    }

    @Bean
    fun databaseInitializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer =
        ConnectionFactoryInitializer().apply {
            setConnectionFactory(connectionFactory)
            setDatabasePopulator(CompositeDatabasePopulator().apply {
                addPopulators(ResourceDatabasePopulator(ClassPathResource("db/migration/V0_1_0__h2-table-email.sql")))
            })
        }

    internal val logger = LoggerFactory.getLogger(this::class.java)
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}