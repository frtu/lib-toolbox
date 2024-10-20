package com.github.frtu.kotlin.llm.os.llm.model

import com.aallam.openai.api.chat.ChatChoice
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ToolCall
import com.aallam.openai.api.core.FinishReason

/**
 * Response message returned from a chat
 */
data class Answer(private val chatChoice: ChatChoice) {
    /**
     * Returning message content
     */
    val content: String?
        get() = message.content

    /**
     * Returning list of tool calls
     */
    val toolCalls: List<ToolCall>?
        get() = message.toolCalls

    /**
     * If answer return tool calls
     */
    val hasToolCall: Boolean
        get() = !toolCalls.isNullOrEmpty()

    /**
     * Returning raw `ChatMessage`
     */
    val message: ChatMessage
        get() = chatChoice.message

    /**
     * Returning the `FinishReason`
     */
    val finishReason: FinishReason?
        get() = chatChoice.finishReason

    /**
     * Internal `ChatChoice` index
     */
    val index: Int
        get() = chatChoice.index

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

