package com.github.frtu.kotlin.ai.os.memory

import com.aallam.openai.api.chat.ChatMessage
import com.github.frtu.kotlin.ai.os.llm.MessageBuilder
import com.github.frtu.kotlin.ai.os.llm.MessageBuilder.createMessage

/**
 * Short term memory - Ephemeral message class is a base unit from a Thread
 * @author frtu
 */
data class Conversation(
    val systemDirective: String? = null,
    private val conversation: MutableList<ChatMessage> = mutableListOf()
) {
    init {
        systemDirective?.let { system(systemDirective) }
    }

    fun system(content: String): Conversation = append(MessageBuilder.system(content))

    fun user(content: String): Conversation = append(MessageBuilder.user(content))

    fun assistant(content: String): Conversation = append(MessageBuilder.assistant(content))

    fun function(functionName: String, content: String): Conversation =
        append(MessageBuilder.function(functionName, content))

    fun addResponse(message: ChatMessage) = +createMessage(
        role = message.role,
        content = message.content.orEmpty(),
        functionCall = message.functionCall,
    )

    fun getMessages(): List<ChatMessage> = conversation

    /**
     * Get Total message in conversation
     */
    fun countMessages(): Int = conversation.size

    /**
     * Allow to trim first messages to free some spaces
     */
    fun trimMessages(): Boolean = true

    fun append(message: ChatMessage): Conversation {
        +message
        return this
    }

    operator fun ChatMessage.unaryPlus() {
        conversation += this
    }
}

