package com.github.frtu.persistence.r2dbc.config

import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import org.slf4j.LoggerFactory
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration

/**
 * Allow to quickly extend and customize R2DBC configuration.
 *
 * Make sure to annotation your Configuration class with :
 * @Configuration
 * @EnableR2dbcRepositories
 *
 * And add a field to autowire URL :
 *
 * @Value("\${application.persistence.url}")
 * lateinit var persistenceUrl: String
 */
abstract class R2dbcConfiguration() : AbstractR2dbcConfiguration() {
    fun connectionFactory(persistenceUrl: String): ConnectionFactory {
        logger.debug("Initialize connection using url:${persistenceUrl}")
        return ConnectionFactories.get(persistenceUrl)
    }

    fun connectionFactory(persistenceProperties: PersistenceProperties, driver: String): ConnectionFactory {
        var options = ConnectionFactoryOptions.builder()
            .option(ConnectionFactoryOptions.HOST, persistenceProperties.host)
            .option(ConnectionFactoryOptions.PORT, persistenceProperties.port)
            .option(ConnectionFactoryOptions.DATABASE, persistenceProperties.database)
            .option(ConnectionFactoryOptions.USER, persistenceProperties.username)
            .option(ConnectionFactoryOptions.PASSWORD, persistenceProperties.password)
            .option(ConnectionFactoryOptions.DRIVER, driver)
            .build();
        return ConnectionFactories.get(options);
    }

    internal val logger = LoggerFactory.getLogger(this::class.java)
}