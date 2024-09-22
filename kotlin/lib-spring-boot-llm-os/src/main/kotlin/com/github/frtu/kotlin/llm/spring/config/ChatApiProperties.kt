package com.github.frtu.kotlin.llm.spring.config

import com.github.frtu.kotlin.llm.os.llm.openai.OpenAiCompatibleChat.Companion.LOCAL_MODEL
import com.github.frtu.kotlin.llm.os.llm.openai.OpenAiCompatibleChat.Companion.LOCAL_URL
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("application.llm.os")
data class ChatApiProperties(
    val apiKey: String? = null,
    val model: String = LOCAL_MODEL, // "mistral"
    val baseUrl: String = LOCAL_URL, // "http://localhost:11434/v1/"
) {
    /**
     * https://platform.openai.com/docs/models/continuous-model-upgrades
     */
    fun isOpenAI() = model.startsWith("gpt-")
    fun validateOpenAIKey() = apiKey != null && apiKey.startsWith("sk-")
}
