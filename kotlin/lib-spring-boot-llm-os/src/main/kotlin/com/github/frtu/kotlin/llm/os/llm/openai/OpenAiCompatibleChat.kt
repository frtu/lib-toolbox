package com.github.frtu.kotlin.llm.os.llm.openai

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
import com.github.frtu.kotlin.llm.os.llm.Chat
import com.github.frtu.kotlin.llm.os.llm.model.Answer
import com.github.frtu.kotlin.llm.os.memory.Conversation
import com.github.frtu.kotlin.llm.os.tool.function.FunctionRegistry
import kotlin.time.Duration.Companion.seconds

/**
 * Compatible OpenAI API
 */
class OpenAiCompatibleChat(
    apiKey: String,
    private val functionRegistry: FunctionRegistry? = null,
    model: String = OPENAI_MODEL,
    baseUrl: String = OPENAI_URL,
    private val defaultEvaluator: ((List<ChatChoice>) -> ChatChoice)? = null,
) : Chat {
    /**
     * Constructor for Local server
     */
    constructor(
        functionRegistry: FunctionRegistry? = null,
        model: String = LOCAL_MODEL,
        baseUrl: String = LOCAL_URL,
        defaultEvaluator: ((List<ChatChoice>) -> ChatChoice)? = null,
    ) : this(
        apiKey = "none",
        functionRegistry = functionRegistry,
        model = model,
        baseUrl = baseUrl,
        defaultEvaluator = defaultEvaluator,
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
        val chatCompletion = send(conversation)

        val chatChoice = evaluator?.let {
            evaluator.invoke(
                chatCompletion.choices
            )
        } ?: defaultEvaluator?.invoke(
            chatCompletion.choices
        )
        ?: throw IllegalStateException("You need to pass an `evaluator` or `defaultEvaluator` to be able to call sendMessage()")
        return Answer(chatChoice)
    }

    suspend fun send(conversation: Conversation): ChatCompletion =
        send(conversation.getMessages())

    suspend fun send(chatMessages: List<ChatMessage>): ChatCompletion {
        // https://github.com/aallam/openai-kotlin/blob/main/guides/ChatFunctionCall.md
        val request = chatCompletionRequest {
            model = modelId
            messages = chatMessages
            functionRegistry?.let {
                functions = functionRegistry.getRegistry()
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
        const val LOCAL_URL = "http://127.0.0.1:5000/v1/"
        const val LOCAL_MODEL = "mistral-7b-instruct-v0.1.Q4_K_M.gguf"

        const val OPENAI_URL = "https://api.openai.com/v1/"
        const val OPENAI_MODEL = "gpt-3.5-turbo"
    }
}

