package com.github.frtu.kotlin.spring.slack.config

import com.github.frtu.kotlin.spring.slack.config.SlackProperties.Companion.SPRING_CONFIG_PREFIX
import com.github.frtu.kotlin.utils.data.ValueObject
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties

@ValueObject
@ConfigurationProperties(prefix = SPRING_CONFIG_PREFIX)
data class SlackProperties(
    val enabled: Boolean = true,
    private val app: SlackAppProperties? = null,
    private val apps: Map<String, SlackAppProperties> = emptyMap(),
) {
    private val _registry: MutableMap<String, SlackAppProperties> = mutableMapOf()

    init {
        if (enabled && app == null && apps.isEmpty()) {
            logger.error("enabled:{} app:{} apps:{}", enabled, app, apps)
            throw IllegalStateException("ATTENTION no Slack 'app' configured ! If you want to deactivate, please change enabled=false")
        }
        app?.let {
            apps[APP_NAME_DEFAULT]?.let { throw IllegalStateException("Attention, app 'default' has been configured, please remove 'application.slack.app'") }
            _registry[APP_NAME_DEFAULT] = app
        }
        _registry.putAll(apps)
    }

    fun defaultApp() = _registry[APP_NAME_DEFAULT]!!

    companion object {
        const val APP_NAME_DEFAULT = "default"
        const val SPRING_CONFIG_PREFIX = "application.slack"
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}