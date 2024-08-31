package com.github.frtu.kotlin.spring.slack.event

import com.github.frtu.logs.core.RpcLogger
import com.github.frtu.logs.core.RpcLogger.kind
import com.github.frtu.logs.core.RpcLogger.requestBody
import com.github.frtu.logs.core.RpcLogger.requestId
import com.github.frtu.logs.core.RpcLogger.uri
import com.github.frtu.logs.core.StructuredLogger.entry
import com.slack.api.bolt.context.builtin.EventContext
import com.slack.api.model.event.MessageEvent
import org.slf4j.LoggerFactory

class MessageEventHandler : AbstractEventHandler<MessageEvent>(MessageEvent::class.java) {
    override fun handleEvent(event: MessageEvent, eventId: String, ctx: EventContext) = with(event) {
        // https://api.slack.com/events/message#message_subtypes
        if (type == "message") {
            // https://api.slack.com/apis/events-api#callback-field
            logger.debug(kind(type), requestId(eventId), uri(channel), entry("event_time", eventTs), requestBody(text))
        }
    }

    private val logger = RpcLogger.create(LoggerFactory.getLogger(this::class.java))
}