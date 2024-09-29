package com.github.frtu.kotlin.spring.slack.event

import com.slack.api.bolt.context.builtin.EventContext
import com.slack.api.methods.request.chat.ChatPostMessageRequest
import com.slack.api.model.Message

open class ThreadManager(
    private val ctx: EventContext,
    private val threadTs: String,
    private val botId: String = ctx.botUserId,
    private val channelId: String = ctx.channelId,
) {
    private val methodsClient = ctx.client()

    // Add reaction to the message
    fun addReaction(emojiName: String) = methodsClient.reactionsAdd { r ->
        r.channel(channelId).timestamp(threadTs)
            .name(emojiName)
    }
    fun removeReaction(emojiName: String) = methodsClient.reactionsRemove { r ->
        r.channel(channelId).timestamp(threadTs)
            .name(emojiName)
    }

    fun retrieveAllMessageNonBot(): List<Message> = retrieveAllMessage { it.user != botId }

    fun retrieveAllMessage(filter: (Message) -> Boolean): List<Message> {
        // Retrieve the conversation thread using conversations.replies
        val repliesResponse = methodsClient.conversationsReplies { r ->
            r.channel(channelId).ts(threadTs)
        } // Pass the thread's root timestamp (message timestamp)
        return repliesResponse.messages.filter(filter)
    }

    fun respond(message: String) = methodsClient.chatPostMessage(
        ChatPostMessageRequest.builder()
            .channel(channelId)
            .threadTs(threadTs) // Respond in the same thread
            .text(message)
            .build()
    )
}