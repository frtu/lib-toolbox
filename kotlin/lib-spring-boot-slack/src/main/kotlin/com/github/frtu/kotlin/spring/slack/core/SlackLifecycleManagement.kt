package com.github.frtu.kotlin.spring.slack.core

import com.slack.api.bolt.socket_mode.SocketModeApp
import com.slack.api.socket_mode.SocketModeClient
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.ContextClosedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SlackLifecycleManagement(
    private val slackApps: List<SlackApp>,
) {
    private val socketModeApps: MutableList<SocketModeApp> = mutableListOf()

    @EventListener(ApplicationReadyEvent::class)
    fun handleAppReady() {
        // Initialize the adapter for Socket Mode
        // with an app-level token and your Bolt app with listeners.
        slackApps.forEach { slackApp ->
            logger.info("== Starting SocketModeApp : ${slackApp.name} ==")
            val socketModeApp = SocketModeApp(
                slackApp.appToken(),
                SocketModeClient.Backend.JavaWebSocket,
                slackApp.app,
            )
            // Using #startAsync() to not block initialisation thread
            socketModeApp.startAsync()
            socketModeApps.add(socketModeApp)
            logger.info("== Started SocketModeApp : ${slackApp.name} ==")
        }
    }

    @EventListener(ContextClosedEvent::class)
    fun handleContextClosedEvent() {
        logger.info("== Closing all SocketModeApp (size: ${socketModeApps.size}) ==")
        socketModeApps.forEach { socketModeApp ->
            socketModeApp.close()
        }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}