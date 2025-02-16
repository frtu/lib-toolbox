package com.github.frtu.kotlin.ai.os.memory

import com.github.frtu.kotlin.ai.os.model.Message

/**
 * Short term memory - Ephemeral message class is a base unit from a Thread
 * @author frtu
 */
data class Conversation(
    val systemDirective: String? = null,
    private val conversation: MutableList<Message> = mutableListOf()
) {
    init {
        systemDirective?.let { system(systemDirective) }
    }

    fun system(content: String): Conversation = append(Message.system(content))

    fun user(content: String): Conversation = append(Message.user(content))

    fun assistant(content: String): Conversation = append(Message.assistant(content))

    fun function(functionName: String, content: String): Conversation =
        append(Message.function(functionName, content))

    fun tool(toolName: String, content: String): Conversation =
        append(Message.tool(toolName, content))

    fun getMessages(): List<Message> = conversation

    fun getLastMessage(): Message = conversation.last()

    /**
     * Get Total message in conversation
     */
    fun countMessages(): Int = conversation.size

    /**
     * Allow to trim first messages to free some spaces
     */
    fun trimMessages(): Boolean = true

    @Deprecated(message = "Use append() instead", replaceWith = ReplaceWith("append"))
    fun addResponse(message: Message) = +message

    fun append(message: Message): Conversation {
        +message
        return this
    }

    operator fun Message.unaryPlus() {
        conversation += this
    }
}

