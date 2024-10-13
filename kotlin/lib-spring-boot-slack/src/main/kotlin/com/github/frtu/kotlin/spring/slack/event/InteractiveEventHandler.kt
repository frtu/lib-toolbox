package com.github.frtu.kotlin.spring.slack.event

import com.github.frtu.kotlin.spring.slack.dialogue.ConversationHandler
import com.github.frtu.kotlin.spring.slack.dialogue.MessageFromThread
import com.github.frtu.kotlin.spring.slack.dialogue.ThreadManager
import com.github.frtu.logs.core.RpcLogger.kind
import com.github.frtu.logs.core.RpcLogger.requestBody
import com.github.frtu.logs.core.RpcLogger.requestId
import com.github.frtu.logs.core.StructuredLogger
import com.github.frtu.logs.core.StructuredLogger.entry
import com.slack.api.bolt.context.builtin.EventContext
import com.slack.api.model.event.AppMentionEvent
import org.slf4j.LoggerFactory

/**
 * Specialisation of `AbstractEventHandler` into a simple conversation handler.
 *
 * It handles feedback to user using an `emojiName`.
 * Pass a message from thread into `ConversationHandler` and to Thread.
 */
class InteractiveEventHandler(
    private val conversationHandler: ConversationHandler<AppMentionEvent>,
    private val emojiName: String = "eyes",
) : AbstractEventHandler<AppMentionEvent>(AppMentionEvent::class.java) {
    override fun handleEvent(event: AppMentionEvent, eventId: String, ctx: EventContext) {
        with(event) {
            logger.debug(
                kind(type), requestId(eventId), entry("event.ts", eventTs),
                entry("user", user), entry("channel.id", channel), requestBody(text)
            )
        }
        with(ThreadManager(ctx, event.threadTs.takeIf { it != null } ?: event.ts)) {
            val reactionResponse = this.addReaction(emojiName)

            val response = conversationHandler.invoke(MessageFromThread(event, eventId), this)
            response?.let { this.respond(it.message) }

            this.removeReaction(emojiName)
            if (reactionResponse.isOk) {
                ctx.ack() // Acknowledge the event
            } else {
                ctx.ackWithJson("{\"error\":\"Failed to add reaction\"}")
            }
        }
    }

    private val logger = StructuredLogger.create(LoggerFactory.getLogger(this::class.java))
}