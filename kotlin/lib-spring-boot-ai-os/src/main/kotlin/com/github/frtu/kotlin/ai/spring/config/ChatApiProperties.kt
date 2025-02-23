package com.github.frtu.kotlin.ai.spring.config

import com.github.frtu.kotlin.ai.os.llm.openai.OpenAiCompatibleChat.Companion.LOCAL_URL
import com.github.frtu.kotlin.ai.os.llm.openai.OpenAiCompatibleChat.Companion.OPENAI_MODEL_4O
import com.github.frtu.kotlin.utils.data.ValueObject
import org.slf4j.event.Level
import org.springframework.boot.context.properties.ConfigurationProperties

@ValueObject
@ConfigurationProperties("application.${LlmOsAutoConfigs.CONFIG_PREFIX}")
data class ChatApiProperties(
    val apiKey: String? = null,
    val model: String = OPENAI_MODEL_4O, // "mistral"
    val baseUrl: String = LOCAL_URL, // "http://localhost:11434/v1/"
    val logLevel: Level = Level.DEBUG,
) {
    /**
     * https://platform.openai.com/docs/models/continuous-model-upgrades
     */
    fun isOpenAI() = model.startsWith("gpt-")
    fun validateOpenAIKey() = apiKey?.startsWith("sk-") ?: false
}
