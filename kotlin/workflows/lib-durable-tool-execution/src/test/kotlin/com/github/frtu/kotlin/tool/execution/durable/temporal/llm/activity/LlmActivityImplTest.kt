package com.github.frtu.kotlin.tool.execution.durable.temporal.llm.activity

import com.github.frtu.kotlin.ai.os.memory.Conversation
import com.github.frtu.kotlin.ai.spring.builder.ChatApiConfigs
import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

internal class LlmActivityImplTest {
    @Test
    fun chat() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        val agent = LlmActivityImpl(
            chat = ChatApiConfigs().chatOllama(
                model = "llama3",
            )
        )
        val systemPrompt = """
                Summarize the current discussion, focusing on the main topics and specific details provided. 
                Highlight the user’s requests, objectives, and preferences, including any recurring themes or ongoing projects. 
                Ensure the summary is concise, accurate, and structured to give a clear overview of the context and progress of the conversation.
            """.trimIndent()

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = agent.chat(
            Conversation(systemPrompt)
                .user("Hey this is fred how are you paul? All good thanks")
        )
        logger.debug("result:$result")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        with(result) {
            shouldNotBeNull()
        }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}