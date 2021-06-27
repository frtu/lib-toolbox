package com.github.frtu.persistence.r2dbc.config

import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

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
@Configuration
@EnableR2dbcRepositories
class R2dbcConfiguration() : AbstractR2dbcConfiguration() {
    @Value("\${application.persistence.url}")
    lateinit var persistenceUrl: String

    @Autowired(required = false)
    var persistenceProperties: PersistenceProperties? = null

    /**
     * Implement default usage using 'application.persistence.url'
     * but can override implementation to pass another url.
     */
    override fun connectionFactory(): ConnectionFactory {
        persistenceProperties?.let {
            val persistenceProperties = persistenceProperties!!
            persistenceProperties.driver?.let {
                return connectionFactory(persistenceProperties, persistenceProperties.driver)
            } ?: {
                logger.warn("You have initialize persistenceProperties:${persistenceProperties} without the driver properties ! Fallback using URL!")
            }
        }
        return connectionFactory(persistenceUrl)
    }

    fun connectionFactory(persistenceUrl: String): ConnectionFactory {
        logger.debug("Initialize connection using url:${persistenceUrl}")
        return ConnectionFactories.get(persistenceUrl)
    }

    fun connectionFactory(persistenceProperties: PersistenceProperties, driver: String): ConnectionFactory {
        logger.debug("Initialize connection using driver:${driver} and persistenceProperties:${persistenceProperties}")
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