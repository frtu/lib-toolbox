package com.github.frtu.sample.persistence.h2

import com.github.frtu.persistence.r2dbc.config.H2R2dbcConfiguration
import com.github.frtu.persistence.r2dbc.config.PostgresJsonR2dbcConfiguration
import com.github.frtu.sample.persistence.r2dbc.basic.BasicR2dbcConfiguration
import com.github.frtu.sample.persistence.r2dbc.basic.Email
import com.github.frtu.sample.persistence.r2dbc.basic.IEmailRepository
import com.github.frtu.sample.persistence.r2dbc.json.EmailJsonDetail
import com.github.frtu.sample.persistence.r2dbc.json.entitytemplate.EmailJsonRepository
import io.r2dbc.spi.ConnectionFactory
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator

@Import(
    H2R2dbcConfiguration::class, // Initialize H2 DB
    BasicR2dbcConfiguration::class // Create Repositories
)
@SpringBootApplication
class ApplicationH2 {
    @Bean
    fun initializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer =
        ConnectionFactoryInitializer().also { initializer ->
            initializer.setConnectionFactory(connectionFactory)
            initializer.setDatabasePopulator(
                CompositeDatabasePopulator().also { populator ->
                    // Add all populators
                    populator.addPopulators(ResourceDatabasePopulator(ClassPathResource("./db/migration/V0_1_0__h2-table-email.sql")))
                }
            )
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
    runApplication<ApplicationH2>(*args)
}
