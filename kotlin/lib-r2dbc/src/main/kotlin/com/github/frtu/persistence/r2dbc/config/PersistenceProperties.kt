package com.github.frtu.persistence.r2dbc.config

import com.github.frtu.persistence.r2dbc.config.PersistenceProperties.Companion.PROPERTIES_PREFIX
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Configuration

@ConstructorBinding
@ConfigurationProperties(PROPERTIES_PREFIX)
data class PersistenceProperties(
    val host: String,
    val port: Int,
    val database: String,
    val username: String,
    val password: String
) {
    companion object {
        const val PROPERTIES_PREFIX = "application.persistence"
    }
}