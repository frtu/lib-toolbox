package com.github.frtu.persistence.r2dbc.config

import io.r2dbc.h2.H2ConnectionConfiguration
import io.r2dbc.h2.H2ConnectionFactory
import io.r2dbc.spi.ConnectionFactory
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
abstract class H2R2dbcConfiguration : AbstractR2dbcConfiguration() {
    fun connectionFactoryInMemory(databaseName: String): ConnectionFactory {
        return H2ConnectionFactory(
            H2ConnectionConfiguration.builder()
                .inMemory(databaseName)
                .build()
        )
    }

    fun connectionFactoryFile(path: String, username: String, password: String): ConnectionFactory {
        return H2ConnectionFactory(
            H2ConnectionConfiguration.builder()
                .file(path)
                .username(username)
                .password(password)
                .build()
        )
    }

    fun connectionFactoryFile(persistenceProperties: PersistenceProperties): ConnectionFactory {
        return connectionFactoryFile(
            persistenceProperties.database,
            persistenceProperties.username,
            persistenceProperties.password
        )
    }
}