package com.github.frtu.kotlin.llm.spring.config

import com.aallam.openai.api.chat.ChatChoice
import com.github.frtu.kotlin.llm.os.llm.Chat
import com.github.frtu.kotlin.llm.os.llm.openai.OpenAiCompatibleChat
import com.github.frtu.kotlin.llm.os.llm.openai.OpenAiCompatibleChat.Companion.LOCAL_MODEL
import com.github.frtu.kotlin.llm.os.llm.openai.OpenAiCompatibleChat.Companion.LOCAL_URL
import com.github.frtu.kotlin.llm.os.tool.FunctionRegistry
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Allow to bootstrap OpenAiCompatibleChat with OpenAI or Ollama
 */
@Configuration
@EnableConfigurationProperties(ChatApiProperties::class)
class ChatApiConfigs {
    @Bean
    @ConditionalOnMissingBean
    fun chatApi(
        // Config
        chatApiProperties: ChatApiProperties,
        // For registration
        functionRegistry: FunctionRegistry? = null,
        // Policy when to choose what ChatChoice
        evaluator: ((List<ChatChoice>) -> ChatChoice)? = null,
    ) = if (chatApiProperties.isOpenAI()) {
        if (!chatApiProperties.validateOpenAIKey()) {
            throw IllegalArgumentException("To use OpenAI model, please configure a correct 'apiKey' properties")
        }
        chatOpenAI(chatApiProperties.apiKey!!, functionRegistry, evaluator)
    } else {
        chatOllama(chatApiProperties.model, chatApiProperties.baseUrl, functionRegistry, evaluator)
    }

    fun chatOpenAI(
        apiKey: String,
        functionRegistry: FunctionRegistry? = null,
        evaluator: ((List<ChatChoice>) -> ChatChoice)? = null,
    ): Chat = OpenAiCompatibleChat(
        apiKey = apiKey,
        functionRegistry = functionRegistry,
        defaultEvaluator = evaluator.takeUnless { it == null } ?: defaultEvaluator,
    )

    fun chatOllama(
        model: String = LOCAL_MODEL, // "mistral"
        baseUrl: String = LOCAL_URL, // "http://localhost:11434/v1/"
        functionRegistry: FunctionRegistry? = null,
        evaluator: ((List<ChatChoice>) -> ChatChoice)? = null,
    ): Chat = OpenAiCompatibleChat(
        functionRegistry = functionRegistry,
        model = model,
        baseUrl = baseUrl,
        defaultEvaluator = evaluator.takeUnless { it == null } ?: defaultEvaluator,
    )

    companion object {
        val defaultEvaluator: (List<ChatChoice>) -> ChatChoice = { chatChoices -> chatChoices.first() }
    }
}