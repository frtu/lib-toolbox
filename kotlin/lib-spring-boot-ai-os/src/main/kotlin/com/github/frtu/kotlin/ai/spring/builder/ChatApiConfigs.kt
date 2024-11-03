package com.github.frtu.kotlin.ai.spring.builder

import com.aallam.openai.api.chat.ChatChoice
import com.github.frtu.kotlin.tool.ToolRegistry
import com.github.frtu.kotlin.ai.os.llm.Chat
import com.github.frtu.kotlin.ai.os.llm.openai.OpenAiCompatibleChat
import com.github.frtu.kotlin.ai.os.llm.openai.OpenAiCompatibleChat.Companion.LOCAL_MODEL
import com.github.frtu.kotlin.ai.os.llm.openai.OpenAiCompatibleChat.Companion.LOCAL_URL
import com.github.frtu.kotlin.ai.spring.config.ChatApiProperties
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
        chatOpenAI(chatApiProperties.apiKey!!, toolRegistry, evaluator)
    } else {
        chatOllama(chatApiProperties.model, chatApiProperties.baseUrl, toolRegistry, evaluator)
    }

    fun chatOpenAI(
        apiKey: String,
        toolRegistry: ToolRegistry? = null,
        evaluator: ((List<ChatChoice>) -> ChatChoice)? = null,
    ): Chat = OpenAiCompatibleChat(
        apiKey = apiKey,
        toolRegistry = toolRegistry,
        defaultEvaluator = evaluator.takeUnless { it == null } ?: defaultEvaluator,
    )

    fun chatOllama(
        model: String = LOCAL_MODEL, // "mistral"
        baseUrl: String = LOCAL_URL, // "http://localhost:11434/v1/"
        toolRegistry: ToolRegistry? = null,
        evaluator: ((List<ChatChoice>) -> ChatChoice)? = null,
    ): Chat = OpenAiCompatibleChat(
        toolRegistry = toolRegistry,
        model = model,
        baseUrl = baseUrl,
        defaultEvaluator = evaluator.takeUnless { it == null } ?: defaultEvaluator,
    )

    companion object {
        val defaultEvaluator: (List<ChatChoice>) -> ChatChoice = { chatChoices -> chatChoices.first() }
    }
}