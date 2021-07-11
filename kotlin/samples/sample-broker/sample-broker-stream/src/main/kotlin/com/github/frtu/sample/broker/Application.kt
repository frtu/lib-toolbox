package com.github.frtu.sample.broker

import com.github.frtu.persistence.r2dbc.config.H2R2dbcConfiguration
import com.github.frtu.sample.persistence.r2dbc.PersistenceConfiguration
import io.r2dbc.spi.ConnectionFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.messaging.Sink
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.core.io.ClassPathResource
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator

@SpringBootApplication
@EnableBinding(Sink::class)
@Import(H2R2dbcConfiguration::class)
@ComponentScan("com.github.frtu.sample.persistence")
class Application {
    @Bean
    fun initializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer =
        ConnectionFactoryInitializer().also { initializer ->
            initializer.setConnectionFactory(connectionFactory)
            initializer.setDatabasePopulator(
                CompositeDatabasePopulator().also { populator ->
                    // Add all populators
                    populator.addPopulators(ResourceDatabasePopulator(ClassPathResource("db/migration/V0_1_0__table-email.sql")))
                }
            )
        }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
