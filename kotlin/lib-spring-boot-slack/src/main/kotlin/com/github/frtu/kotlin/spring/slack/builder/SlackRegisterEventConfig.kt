package com.github.frtu.kotlin.spring.slack.builder

import com.github.frtu.kotlin.spring.slack.core.SlackEventHandlerRegistry
import com.slack.api.bolt.App
import com.slack.api.bolt.handler.BoltEventHandler
import com.slack.api.model.event.Event
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
    fun slackEventHandlerRegistryRegistration(app: App, slackEventHandlerRegistry: SlackEventHandlerRegistry): String {
        // Register all event available as Spring Beans
        slackEventHandlerRegistry.getAll().forEach { (eventType, handler) ->
            @Suppress("UNCHECKED_CAST")
            app.event(eventType as Class<Event>, handler as BoltEventHandler<Event>)
        }
        return "OK"
    }

    companion object {
        const val CONFIG_PREFIX = "events"
    }
}