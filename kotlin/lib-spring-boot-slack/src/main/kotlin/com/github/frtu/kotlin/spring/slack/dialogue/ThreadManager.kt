package com.github.frtu.kotlin.spring.slack.dialogue

import com.github.frtu.logs.core.RpcLogger.kind
import com.github.frtu.logs.core.RpcLogger.requestBody
import com.github.frtu.logs.core.RpcLogger.responseBody
import com.github.frtu.logs.core.StructuredLogger
import com.github.frtu.logs.core.StructuredLogger.entry
import com.slack.api.bolt.context.builtin.EventContext
import com.slack.api.methods.kotlin_extension.request.chat.blocks
import com.slack.api.methods.request.chat.ChatPostMessageRequest
import com.slack.api.methods.response.chat.ChatPostMessageResponse
import com.slack.api.model.Message
import com.slack.api.model.kotlin_extension.block.dsl.LayoutBlockDsl
import org.slf4j.LoggerFactory

/**
 * Providing advanced interaction to the original Thread
 * @since 2.0.5
 */
open class ThreadManager(
    val ctx: EventContext,
    private val threadTs: String,
    private val botId: String = ctx.botUserId,
    private val channelId: String = ctx.channelId,
) {
    private val methodsClient = ctx.client()

    /** Add reaction to the thread */
    fun addReaction(emojiName: String) = methodsClient.reactionsAdd { r ->
        logger.trace("Adding emojiName:$emojiName to channelId:$channelId")
        r.channel(channelId).timestamp(threadTs)
            .name(emojiName)
    }

    /** Remove reaction to the thread */
    fun removeReaction(emojiName: String) = methodsClient.reactionsRemove { r ->
        logger.trace("Removing emojiName:$emojiName to channelId:$channelId")
        r.channel(channelId).timestamp(threadTs)
            .name(emojiName)
    }

    /** Retrieve all the message FROM the thread that is not sent by bot */
    fun retrieveAllMessagesNonBot(): List<Message> = retrieveAllMessages { msg ->
        val result = (msg.user != botId && msg.type == "message")
        structuredLogger.trace(
            kind(msg.type), entry("user", msg.user), entry("channel.id", channelId),
            responseBody(if (result) "keep" else "trimmed"), requestBody(msg.text),
        )
        result
    }

    /** Retrieve all the message FROM the thread */
    fun retrieveAllMessages(filter: (Message) -> Boolean): List<Message> {
        // Retrieve the conversation thread using conversations.replies
        val repliesResponse = methodsClient.conversationsReplies { r ->
            r.channel(channelId).ts(threadTs)
        } // Pass the thread's root timestamp (message timestamp)
        return repliesResponse.messages.filter(filter)
    }

    /** Respond TO the current thread */
    fun respond(messageToThread: MessageToThread): ChatPostMessageResponse = respond(messageToThread.message)

    /** Respond TO the current thread */
    fun respond(messageString: String): ChatPostMessageResponse = respond {
        it.text(messageString)
    }

    /**
     * Respond TO the current thread
     * @see <a href="https://tools.slack.dev/java-slack-sdk/guides/composing-messages/#block-kit-kotlin-dsl">block kit kotlin dsl</a>
     */
    fun respondForm(builder: LayoutBlockDsl.() -> Unit): ChatPostMessageResponse = respond {
        it.blocks(builder)
    }

    /** Execute ON the current thread */
    fun respond(handler: (ChatPostMessageRequest.ChatPostMessageRequestBuilder) -> Unit): ChatPostMessageResponse {
        val requestBuilder = ChatPostMessageRequest.builder()
            .channel(channelId)
            .threadTs(threadTs) // Respond in the same thread
        handler.invoke(requestBuilder)
        return methodsClient.chatPostMessage(requestBuilder.build())
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val structuredLogger = StructuredLogger.create(logger)
}