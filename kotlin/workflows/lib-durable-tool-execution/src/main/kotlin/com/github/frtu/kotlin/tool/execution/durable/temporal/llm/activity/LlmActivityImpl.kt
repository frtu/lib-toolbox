package com.github.frtu.kotlin.tool.execution.durable.temporal.llm.activity

import com.github.frtu.kotlin.ai.os.llm.Chat
import com.github.frtu.kotlin.ai.os.memory.Conversation
import kotlinx.coroutines.runBlocking

/**
 * Encapsulate an LLM executor as an Activity
 *
 * @author Frédéric TU
 * @since 2.0.16
 */
class LlmActivityImpl(
    /** Engine containing model version */
    private val chat: Chat,
) : LlmActivity {
    override fun chat(conversation: Conversation): String = runBlocking {
        chat.sendMessage(conversation).content!!
    }
}