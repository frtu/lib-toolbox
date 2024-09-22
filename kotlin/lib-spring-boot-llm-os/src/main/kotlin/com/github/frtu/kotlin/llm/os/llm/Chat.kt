package com.github.frtu.kotlin.llm.os.llm

import com.github.frtu.kotlin.llm.os.llm.model.Answer
import com.github.frtu.kotlin.llm.os.memory.Conversation

/**
 * Chat completion API taking a Conversation & returning an Answer
 */
interface Chat {
    suspend fun sendMessage(
        conversation: Conversation,
    ): Answer
}