package com.github.frtu.persistence.r2dbc.config

import io.r2dbc.h2.H2ConnectionConfiguration
import io.r2dbc.h2.H2ConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration

/**
 * Allow to quickly extend and customize R2DBC configuration.
 *
 * Make sure to annotation your Configuration class with :
 * @Configuration
 * @EnableR2dbcRepositories
 *
 * User either :
 * connectionFactoryInMemory("test")
 * connectionFactoryFile("./path", "sa", "password")
 */
class H2R2dbcConfiguration : AbstractR2dbcConfiguration() {
    @Autowired(required = false)
    var persistenceProperties: PersistenceProperties? = null

    /**
     * Implement default usage using 'application.persistence.url'
     * but can override implementation to pass another url.
     */
    override fun connectionFactory(): ConnectionFactory {
        persistenceProperties?.let {
            return connectionFactoryFile(persistenceProperties!!)
        }
        throw IllegalArgumentException("You need to initialize persistenceProperties to be able to use it!")
    }

    fun connectionFactoryInMemory(databaseName: String): ConnectionFactory {
        logger.debug("Initialize inMemory db using databaseName:${databaseName}")
        return H2ConnectionFactory(
            H2ConnectionConfiguration.builder()
                .inMemory(databaseName)
                .build()
        )
    }

    fun connectionFactoryFile(path: String, username: String, password: String): ConnectionFactory {
        logger.debug("Initialize file db using path:${path} username:${username} password.size:${password.length}")
        return H2ConnectionFactory(
            H2ConnectionConfiguration.builder()
                .file(path)
                .username(username)
                .password(password)
                .build()
        )
    }

    fun connectionFactoryFile(persistenceProperties: PersistenceProperties): ConnectionFactory {
        logger.debug("Initialize file db using persistenceProperties:${persistenceProperties}")
        return connectionFactoryFile(
            persistenceProperties.database,
            persistenceProperties.username,
            persistenceProperties.password
        )
    }

    internal val logger = LoggerFactory.getLogger(this::class.java)
}