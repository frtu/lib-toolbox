package com.github.frtu.persistence.r2dbc.config

import com.github.frtu.persistence.r2dbc.config.PersistenceProperties.Companion.PROPERTIES_PREFIX
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import java.lang.IllegalStateException
import javax.annotation.PostConstruct

@Configuration
@EnableR2dbcRepositories
@EnableConfigurationProperties(PersistenceProperties::class)
class PostgresR2dbcConfiguration : AbstractR2dbcConfiguration() {
    internal val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired(required = false)
    var persistenceProperties: PersistenceProperties? = null

    @PostConstruct
    fun persistencePropertiesNonNull() = persistenceProperties
        ?: throw IllegalStateException("You must configure PostgresQL PersistenceProperties in application.properties/yaml using prefix ${PROPERTIES_PREFIX}.*")

    override fun connectionFactory(): ConnectionFactory {
        val postgresqlConnectionConfiguration = postgresqlConnectionConfigurationBuilder().build()
        logger.debug("Initialize connection using postgresqlConnectionConfiguration:${postgresqlConnectionConfiguration}")
        return PostgresqlConnectionFactory(postgresqlConnectionConfiguration)
    }

    internal fun postgresqlConnectionConfigurationBuilder(): PostgresqlConnectionConfiguration.Builder {
        val properties = persistencePropertiesNonNull()
        return PostgresqlConnectionConfiguration.builder()
            .host(properties.host)
            .port(properties.port)
            .database(properties.database)
            .username(properties.username)
            .password(properties.password)
    }
}

