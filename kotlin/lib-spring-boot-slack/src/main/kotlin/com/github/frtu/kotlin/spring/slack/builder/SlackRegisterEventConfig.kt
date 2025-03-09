package com.github.frtu.kotlin.spring.slack.builder

import com.github.frtu.kotlin.spring.slack.core.SlackApp
import com.github.frtu.kotlin.spring.slack.core.SlackEventHandlerRegistry
import com.slack.api.bolt.App
import com.slack.api.bolt.handler.BoltEventHandler
import com.slack.api.model.event.Event
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@ConditionalOnProperty(
    prefix = "application.slack.${SlackRegisterEventConfig.CONFIG_PREFIX}", name = ["enabled"],
    havingValue = "true", matchIfMissing = true,
)
@Configuration
@Import(SlackEventHandlerRegistry::class)
class SlackRegisterEventConfig {
    @Bean
    @Qualifier("SlackEventHandlerRegistryRegistration")
    fun slackEventHandlerRegistryRegistration(
        slackApps: List<SlackApp>,
        slackEventHandlerRegistry: SlackEventHandlerRegistry,
    ): String {
        slackApps.forEach { slackApp ->
            // Register all event available as Spring Beans
            slackEventHandlerRegistry.getAll().forEach { (eventType, handler) ->
                logger.info("Enabling Event {} with class:{}", eventType, handler.javaClass)
                @Suppress("UNCHECKED_CAST")
                slackApp.app.event(eventType as Class<Event>, handler as BoltEventHandler<Event>)
            }
        }
        return "OK"
    }

    companion object {
        const val CONFIG_PREFIX = "events"
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}