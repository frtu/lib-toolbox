package com.github.frtu.kotlin.llm.os.llm.model

import com.aallam.openai.api.chat.ChatChoice
import com.aallam.openai.api.chat.ChatMessage

/**
 * Response message returned from a chat
 */
data class Answer(private val chatChoice: ChatChoice) {
    val message: ChatMessage
        get() = chatChoice.message

    val content: String?
        get() = chatChoice.message.content

    val invokeFunction: InvokeFunction?
        get() = with(chatChoice.message) {
            functionCall?.let {
                // Function is returned correctly parsed
                InvokeFunction(it)
            } ?: content?.let {
                // Manual parsing using content
                parseContent(content!!)
            }
        }
}

