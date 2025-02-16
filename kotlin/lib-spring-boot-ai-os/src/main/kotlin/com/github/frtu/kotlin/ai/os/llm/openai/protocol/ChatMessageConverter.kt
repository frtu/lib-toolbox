package com.github.frtu.kotlin.ai.os.llm.openai.protocol

import com.aallam.openai.api.chat.ChatMessage
import com.github.frtu.kotlin.ai.os.model.Message

object ChatMessageExt

fun Message.toChatMessage() = ChatMessage(
    role = role.toChatRole(),
    content = simpleContent,
    name = name,
)

fun ChatMessage.toMessage() = Message(
    role = role.toRole(),
    simpleContent = content,
    name = name,
)