package com.github.frtu.kotlin.ai.spring.config

import com.github.frtu.kotlin.ai.os.llm.openai.OpenAiCompatibleChat.Companion.LOCAL_MODEL
import com.github.frtu.kotlin.ai.os.llm.openai.OpenAiCompatibleChat.Companion.LOCAL_URL
import com.github.frtu.kotlin.utils.data.ValueObject
import org.springframework.boot.context.properties.ConfigurationProperties

@ValueObject
@ConfigurationProperties("application.ai.os.llm")
class ChatApiProperties(
    val apiKey: String? = null,
    val model: String = LOCAL_MODEL, // "mistral"
    val baseUrl: String = LOCAL_URL, // "http://localhost:11434/v1/"
) {
    /**
     * https://platform.openai.com/docs/models/continuous-model-upgrades
     */
    fun isOpenAI() = model.startsWith("gpt-")
    fun validateOpenAIKey() = apiKey?.startsWith("sk-") ?: false
}
