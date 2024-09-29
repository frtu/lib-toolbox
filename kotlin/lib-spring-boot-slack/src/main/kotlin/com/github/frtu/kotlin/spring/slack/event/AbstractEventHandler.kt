package com.github.frtu.kotlin.spring.slack.event

import com.github.frtu.logs.core.RpcLogger
import com.github.frtu.logs.core.RpcLogger.kind
import com.github.frtu.logs.core.RpcLogger.requestId
import com.github.frtu.logs.core.StructuredLogger.entry
import com.slack.api.app_backend.events.payload.EventsApiPayload
import com.slack.api.bolt.context.builtin.EventContext
import com.slack.api.bolt.handler.BoltEventHandler
import com.slack.api.bolt.response.Response
import com.slack.api.model.event.Event
import org.slf4j.LoggerFactory

abstract class AbstractEventHandler<E : Event>(
    private val eventClass: Class<E>,
) : BoltEventHandler<E> {
    override fun apply(eventsApiPayload: EventsApiPayload<E>, ctx: EventContext): Response {
        val event = eventsApiPayload.event
        logger.trace(
            kind(event.type), requestId(eventsApiPayload.eventId), entry("channel.id", ctx.channelId),
            entry("bot.user.id", ctx.botUserId), entry("bot.scope", ctx.botScopes)
        )
        handleEvent(event, eventsApiPayload.eventId, ctx)
        return ctx.ack()
    }

    abstract fun handleEvent(event: E, eventId: String, ctx: EventContext)

    fun toPair(): Pair<Class<E>, BoltEventHandler<E>> = Pair(eventClass, this)

    private val logger = RpcLogger.create(LoggerFactory.getLogger(this::class.java))
}

