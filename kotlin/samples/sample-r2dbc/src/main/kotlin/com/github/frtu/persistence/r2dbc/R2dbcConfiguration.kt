package com.github.frtu.persistence.r2dbc

import io.r2dbc.h2.H2ConnectionConfiguration
import io.r2dbc.h2.H2ConnectionFactory
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator

@Configuration
@EnableR2dbcRepositories
class R2dbcConfiguration : AbstractR2dbcConfiguration() {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(R2dbcConfiguration::class.java)
    }
    
    @Value("\${application.persistence.url}")
    lateinit var persistenceUrl: String

    override fun connectionFactory(): ConnectionFactory {
        LOGGER.debug("Initialize connection using url:${persistenceUrl}")
        return ConnectionFactories.get(persistenceUrl)
    }

    internal fun h2ConnectionFactory(): ConnectionFactory {
        H2ConnectionFactory.inMemory("test");
        return H2ConnectionFactory(
            H2ConnectionConfiguration.builder()
                .inMemory("test")
//                .file("./test")
//                .username("sa")
//                .password("password")
                .build()
        )
    }

    internal fun postgresConnectionFactory(): ConnectionFactory {
        return PostgresqlConnectionFactory(
            PostgresqlConnectionConfiguration.builder()
                .host("localhost")
                .port(5432)
                .database("notification")
                .username("user_admin")
                .password("pass")
                //.codecRegistrar(EnumCodec.builder().withEnum("post_status", Post.Status.class).build())
                .build()
        )
    }

    internal fun customConnectionFactory(): ConnectionFactory {
        var options = ConnectionFactoryOptions.builder()
            .option(ConnectionFactoryOptions.HOST, "localhost")
            .option(ConnectionFactoryOptions.PORT, 5432)
            .option(ConnectionFactoryOptions.DATABASE, "notification")
            .option(ConnectionFactoryOptions.USER, "user_admin")
            .option(ConnectionFactoryOptions.PASSWORD, "pass")
            .option(ConnectionFactoryOptions.DRIVER, "postgresql")
            //.option(PostgresqlConnectionFactoryProvider.OPTIONS, Map.of("lock_timeout", "30s"))
            .build();
        return ConnectionFactories.get(options);
    }

    @Bean
    fun initializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer {
        val initializer = ConnectionFactoryInitializer()
        initializer.setConnectionFactory(connectionFactory)
        val populator = CompositeDatabasePopulator()
        populator.addPopulators(ResourceDatabasePopulator(ClassPathResource("./db/migration/V0_1_0__table-email.sql")))
        initializer.setDatabasePopulator(populator)
        return initializer
    }
}