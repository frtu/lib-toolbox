package com.github.frtu.kotlin.spring.slack.config

import com.github.frtu.kotlin.spring.slack.core.SlackEventHandlerRegistry
import com.slack.api.bolt.App
import com.slack.api.bolt.handler.BoltEventHandler
import com.slack.api.model.event.Event
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
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
}