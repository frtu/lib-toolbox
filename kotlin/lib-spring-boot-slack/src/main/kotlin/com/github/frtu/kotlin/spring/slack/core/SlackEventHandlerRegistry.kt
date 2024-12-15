package com.github.frtu.kotlin.spring.slack.core

import com.slack.api.bolt.handler.BoltEventHandler
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Repository

/**
 * Registry for all Event type class
 *
 * @see <a href="https://api.slack.com/events/message">Event type</a>
 * @see <a href="https://oss.sonatype.org/service/local/repositories/releases/archive/com/slack/api/slack-api-model/1.42.0/slack-api-model-1.42.0-javadoc.jar/!/com/slack/api/model/event/Event.html">All Event types</a>
 */
@Lazy
@Repository
class SlackEventHandlerRegistry(
    boltEventHandlers: List<Pair<Class<*>, BoltEventHandler<*>>> = emptyList()
) {
    private val registry: Map<Class<*>, BoltEventHandler<*>> = boltEventHandlers.toMap()

    @PostConstruct
    fun init() {
        registry.entries.forEach { (eventType, handler) ->
            logger.trace("Registered event handler for [${eventType.simpleName}] for type ${handler.javaClass}")
        }
    }

    fun getAll(): Set<Map.Entry<Class<*>, BoltEventHandler<*>>> = registry.entries

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}