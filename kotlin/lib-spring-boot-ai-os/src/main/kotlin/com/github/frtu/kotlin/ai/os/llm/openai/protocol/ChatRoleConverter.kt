package com.github.frtu.kotlin.ai.os.llm.openai.protocol

import com.aallam.openai.api.chat.ChatRole
import com.github.frtu.kotlin.ai.os.model.Role

object ChatRoleConverter

fun Role.toChatRole() = when(this) {
    Role.SYSTEM -> ChatRole.System
    Role.USER -> ChatRole.User
    Role.ASSISTANT -> ChatRole.Assistant
    Role.FUNCTION -> ChatRole.Function
    Role.TOOL -> ChatRole.Tool
}

fun ChatRole.toRole() = when(this) {
    ChatRole.System -> Role.SYSTEM
    ChatRole.User -> Role.USER
    ChatRole.Assistant -> Role.ASSISTANT
    ChatRole.Function -> Role.FUNCTION
    ChatRole.Tool -> Role.TOOL
    else -> throw IllegalStateException("Not able to map ChatRole:$this")
}