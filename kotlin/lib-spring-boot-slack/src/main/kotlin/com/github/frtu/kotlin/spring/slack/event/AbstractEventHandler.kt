package com.github.frtu.kotlin.spring.slack.event

import com.github.frtu.logs.core.RpcLogger
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
        logger.trace("Message received: channelId=[${ctx.channelId}] botUserId=[${ctx.botUserId}] botScopes=[${ctx.botScopes}]")
        handleEvent(eventsApiPayload.event, eventsApiPayload.eventId, ctx)
        return ctx.ack()
    }

    abstract fun handleEvent(event: E, eventId: String, ctx: EventContext)

    fun toPair(): Pair<Class<E>, BoltEventHandler<E>> = Pair(eventClass, this)

    private val logger = RpcLogger.create(LoggerFactory.getLogger(this::class.java))
}

