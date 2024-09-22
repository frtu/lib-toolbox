package com.github.frtu.kotlin.llm.os.llm

import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.chat.FunctionCall

/**
 * Encapsulate aallam chat message builder and role into function
 */
object MessageBuilder {
    fun system(content: String) = createMessage(ChatRole.System, content)

    fun user(content: String) = createMessage(ChatRole.User, content)

    fun assistant(content: String) = createMessage(ChatRole.Assistant, content)

    fun function(functionName: String, content: String) = createMessage(ChatRole.Function, content, functionName)

    fun createMessage(
        role: ChatRole,
        content: String? = null,
        name: String? = null,
        functionCall: FunctionCall? = null,
    ) = ChatMessage(role, content, name, functionCall)
}