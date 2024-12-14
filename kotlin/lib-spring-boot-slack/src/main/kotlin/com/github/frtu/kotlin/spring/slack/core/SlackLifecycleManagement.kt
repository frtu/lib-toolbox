package com.github.frtu.kotlin.spring.slack.core

import com.github.frtu.kotlin.spring.slack.builder.SlackInitAppConfig.Companion.KEY_APP_TOKEN
import com.slack.api.bolt.App
import com.slack.api.bolt.socket_mode.SocketModeApp
import com.slack.api.socket_mode.SocketModeClient
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.ContextClosedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SlackLifecycleManagement(
    private val app: App,
    @Qualifier(KEY_APP_TOKEN)
    private val appToken: String,
) {
    private lateinit var socketModeApp: SocketModeApp

    @EventListener(ApplicationReadyEvent::class)
    fun handleAppReady() {
        logger.info("== Starting SocketModeApp ==")
        // Initialize the adapter for Socket Mode
        // with an app-level token and your Bolt app with listeners.
        socketModeApp = SocketModeApp(
            appToken,
            SocketModeClient.Backend.JavaWebSocket,
            app,
        )
        // Using #startAsync() to not block initialisation thread
        socketModeApp.startAsync()
        logger.info("== Started SocketModeApp ==")
    }

    @EventListener(ContextClosedEvent::class)
    fun handleContextClosedEvent() {
        logger.info("== Closing SocketModeApp ==")
        socketModeApp.close()
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}