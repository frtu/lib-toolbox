package com.github.frtu.kotlin.ai.os.llm.openai

import com.aallam.openai.api.chat.ChatChoice
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.FunctionMode
import com.aallam.openai.api.chat.chatCompletionRequest
import com.aallam.openai.api.embedding.embeddingRequest
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.aallam.openai.client.OpenAIHost
import com.github.frtu.kotlin.tool.ToolRegistry
import com.github.frtu.kotlin.ai.os.llm.Chat
import com.github.frtu.kotlin.ai.os.llm.model.Answer
import com.github.frtu.kotlin.ai.os.llm.openai.protocol.toChatCompletionFunction
import com.github.frtu.kotlin.ai.os.llm.openai.protocol.toChatMessage
import com.github.frtu.kotlin.ai.os.memory.Conversation
import kotlin.time.Duration.Companion.seconds
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

/**
 * Compatible OpenAI API
 */
class OpenAiCompatibleChat(
    apiKey: String,
    private val toolRegistry: ToolRegistry? = null,
    model: String = OPENAI_MODEL,
    baseUrl: String = OPENAI_URL,
    private val defaultEvaluator: ((List<ChatChoice>) -> ChatChoice)? = null,
    private val logLevel: Level = Level.DEBUG,
) : Chat {
    /**
     * Constructor for Local server
     */
    constructor(
        toolRegistry: ToolRegistry? = null,
        model: String = LOCAL_MODEL,
        baseUrl: String = LOCAL_URL,
        defaultEvaluator: ((List<ChatChoice>) -> ChatChoice)? = null,
        logLevel: Level = Level.DEBUG,
    ) : this(
        apiKey = "none",
        toolRegistry = toolRegistry,
        model = model,
        baseUrl = baseUrl,
        defaultEvaluator = defaultEvaluator,
        logLevel = logLevel,
    )

    private val modelId = ModelId(model)
    private val openAI = OpenAI(
        OpenAIConfig(
            token = apiKey,
            timeout = Timeout(socket = 60.seconds),
            host = OpenAIHost(baseUrl = baseUrl)
        )
    )

    override suspend fun sendMessage(
        conversation: Conversation,
    ): Answer = sendMessage(conversation, null)

    suspend fun sendMessage(
        conversation: Conversation,
        evaluator: ((List<ChatChoice>) -> ChatChoice)? = null,
    ): Answer {
        when (logLevel) {
            Level.DEBUG -> logger.debug("Conversation - Receiving last message on {} history size:{}", conversation.getLastMessage(), conversation.countMessages())
            else -> logger.info("Conversation - Receiving last message on {} history size:{}", conversation.getLastMessage(), conversation.countMessages())
        }

        val chatCompletion = send(conversation)

        val chatChoice = evaluator?.let {
            evaluator.invoke(
                chatCompletion.choices
            )
        } ?: defaultEvaluator?.invoke(
            chatCompletion.choices
        )
        ?: throw IllegalStateException("You need to pass an `evaluator` or `defaultEvaluator` to be able to call sendMessage()")
        return Answer(chatChoice).also { answer: Answer ->
            when (logLevel) {
                Level.DEBUG -> logger.debug("Receiving response:{}", answer)
                else -> logger.info("Receiving response:{}", answer)
            }
        }
    }

    suspend fun send(conversation: Conversation): ChatCompletion =
        send(conversation.getMessages().map { it.toChatMessage() })

    suspend fun send(chatMessages: List<ChatMessage>): ChatCompletion {
        // https://github.com/aallam/openai-kotlin/blob/main/guides/ChatFunctionCall.md
        val request = chatCompletionRequest {
            model = modelId
            messages = chatMessages
            toolRegistry?.let {
                functions = toolRegistry.getAll().map { it.toChatCompletionFunction() }
                functionCall = FunctionMode.Auto
            }
        }
        return openAI.chatCompletion(request)
    }

    fun createEmbedding() {
        val embeddingRequest = embeddingRequest {
            this.model = ModelId("text-embedding-ada-002")
        }
//
//        model = "BAAI/bge-small-en-v1.5"
//        return HuggingFaceEmbedding(model_name=model)
    }

    companion object {
        // Using Ollama https://ollama.com/blog/ollama-is-now-available-as-an-official-docker-image
        const val LOCAL_URL = "http://localhost:11434/v1/"
        const val LOCAL_MODEL = "mistral"

        const val OPENAI_URL = "https://api.openai.com/v1/"
        const val OPENAI_MODEL = "gpt-3.5-turbo"

        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}

