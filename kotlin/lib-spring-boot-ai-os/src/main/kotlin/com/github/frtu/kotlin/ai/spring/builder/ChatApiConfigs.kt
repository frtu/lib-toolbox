package com.github.frtu.kotlin.ai.spring.builder

import com.aallam.openai.api.chat.ChatChoice
import com.github.frtu.kotlin.ai.os.llm.Chat
import com.github.frtu.kotlin.ai.os.llm.openai.OpenAiCompatibleChat
import com.github.frtu.kotlin.ai.os.llm.openai.OpenAiCompatibleChat.Companion.LOCAL_MODEL
import com.github.frtu.kotlin.ai.os.llm.openai.OpenAiCompatibleChat.Companion.LOCAL_URL
import com.github.frtu.kotlin.ai.os.llm.openai.OpenAiCompatibleChat.Companion.OPENAI_MODEL_4O
import com.github.frtu.kotlin.ai.spring.config.ChatApiProperties
import com.github.frtu.kotlin.tool.ToolRegistry
import org.slf4j.event.Level
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Allow to bootstrap OpenAiCompatibleChat with OpenAI or Ollama
 */
@Configuration
class ChatApiConfigs {
    @Bean
    @ConditionalOnMissingBean
    fun chatApi(
        // Config
        chatApiProperties: ChatApiProperties,
        // For registration
        toolRegistry: ToolRegistry? = null,
        // Policy when to choose what ChatChoice
        evaluator: ((List<ChatChoice>) -> ChatChoice)? = null,
    ) = if (chatApiProperties.isOpenAI()) {
        if (!chatApiProperties.validateOpenAIKey()) {
            throw IllegalArgumentException("To use OpenAI model, please configure a correct 'apiKey' properties")
        }
        chatOpenAI(chatApiProperties.apiKey!!, toolRegistry, chatApiProperties.model, evaluator, chatApiProperties.logLevel)
    } else {
        chatOllama(chatApiProperties.model, chatApiProperties.baseUrl, toolRegistry, evaluator, chatApiProperties.logLevel)
    }

    fun chatOpenAI(
        apiKey: String,
        toolRegistry: ToolRegistry? = null,
        model: String = OPENAI_MODEL_4O,
        evaluator: ((List<ChatChoice>) -> ChatChoice)? = null,
        logLevel: Level = Level.DEBUG,
    ): Chat = OpenAiCompatibleChat(
        apiKey = apiKey,
        model = model,
        toolRegistry = toolRegistry,
        defaultEvaluator = evaluator.takeUnless { it == null } ?: defaultEvaluator,
        logLevel = logLevel,
    )

    fun chatOllama(
        model: String = LOCAL_MODEL, // "mistral"
        baseUrl: String = LOCAL_URL, // "http://localhost:11434/v1/"
        toolRegistry: ToolRegistry? = null,
        evaluator: ((List<ChatChoice>) -> ChatChoice)? = null,
        logLevel: Level = Level.DEBUG,
    ): Chat = OpenAiCompatibleChat(
        toolRegistry = toolRegistry,
        model = model,
        baseUrl = baseUrl,
        defaultEvaluator = evaluator.takeUnless { it == null } ?: defaultEvaluator,
        logLevel = logLevel,
    )

    companion object {
        val defaultEvaluator: (List<ChatChoice>) -> ChatChoice = { chatChoices -> chatChoices.first() }
    }
}