package com.github.frtu.kotlin.spring.slack.core

import com.slack.api.bolt.App
import com.slack.api.bolt.AppConfig
import com.slack.api.bolt.handler.BoltEventHandler
import com.slack.api.bolt.socket_mode.SocketModeApp
import com.slack.api.model.event.Event
import com.slack.api.socket_mode.SocketModeClient
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.ContextClosedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SlackLifecycleManagement(
    private val appConfig: AppConfig,
    @Qualifier(KEY_APP_TOKEN)
    private val appToken: String,
    private val slackCommandRegistry: SlackCommandRegistry,
    private val slackEventHandlerRegistry: SlackEventHandlerRegistry,
) {
    private lateinit var app: App
    private lateinit var socketModeApp: SocketModeApp

    @EventListener(ApplicationReadyEvent::class)
    fun handleAppReady() {
        logger.info("== Receiving ApplicationReadyEvent ==")

        // Initialize the App and register listeners
        app = App(appConfig)

        // Register all commands available as Spring Beans
        slackCommandRegistry.getAll().forEach { (name, command) ->
            app.command("/$name", command)
        }
        slackEventHandlerRegistry.getAll().forEach { (eventType, handler) ->
            @Suppress("UNCHECKED_CAST")
            app.event(eventType as Class<Event>, handler as BoltEventHandler<Event>)
        }

        // Initialize the adapter for Socket Mode
        // with an app-level token and your Bolt app with listeners.
        socketModeApp = SocketModeApp(
            appToken,
            SocketModeClient.Backend.JavaWebSocket,
            app,
        )
        // #start() method establishes a new WebSocket connection and then blocks the current thread.
        // If you do not want to block this thread, use #startAsync() instead.
        socketModeApp.start()
    }

    @EventListener(ContextClosedEvent::class)
    fun handleContextClosedEvent() {
        logger.info("== Receiving ContextClosedEvent ==")
        socketModeApp.close()
    }

    companion object {
        const val KEY_APP_TOKEN = "APP_TOKEN_QUALIFIER"
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}