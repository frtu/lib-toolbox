package com.github.frtu.kotlin.llm.os.model

/**
 * Message class is a base unit from a Thread
 * @author frtu
 */
data class Message(
    val role: Role,
    val content: String,
)

//fun userMessage(content: String) = Message(Role.USER, content)
//fun systemMessage(content: String) = Message(Role.SYSTEM, content)