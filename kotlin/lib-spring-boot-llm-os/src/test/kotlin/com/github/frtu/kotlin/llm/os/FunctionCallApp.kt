package com.github.frtu.kotlin.llm.os

import com.github.frtu.kotlin.llm.os.llm.Chat
import com.github.frtu.kotlin.llm.os.llm.openai.OpenAiCompatibleChat
import com.github.frtu.kotlin.llm.os.memory.Conversation
import com.github.frtu.kotlin.llm.os.tool.Tool
import com.github.frtu.kotlin.llm.os.tool.function.FunctionRegistry
import com.github.frtu.kotlin.llm.os.tool.function.registry
import com.github.frtu.kotlin.utils.io.toJsonNode
import sample.tool.function.CurrentWeatherFunction
import sample.tool.function.WeatherForecastFunction

suspend fun main() {
    val apiKey = "sk-xxx"
    val functionRegistry = buildFunctionRegistry()

    // === Choose between OpenAI & open source ===
    val chat = chatOpenAI(apiKey, functionRegistry)
//    val chat = chatOllama(functionRegistry)

    // === Start conversation ===
    with(Conversation()) {
        system("Don't make assumptions about what values to plug into functions. Ask for clarification if a user request is ambiguous.")

        chat.sendMessage(user("What's the weather like in Glasgow, Scotland over the next x days?"))
        val response = chat.sendMessage(user("5 days"))
        println(response)

        val message = response.message
        message.functionCall?.let { functionCall ->
            this.addResponse(message)

            val functionToCall: Tool = functionRegistry.getFunction(functionCall.name)

            val functionArgs = functionCall.arguments.toJsonNode()
            println("Request:${functionArgs.toPrettyString()}")
            val result = functionToCall.execute(functionArgs)
//            val location = functionArgs["location"].textValue()
//            val unit = functionArgs["unit"]?.textValue() ?: "fahrenheit"
//            val numberOfDays = functionArgs["numberOfDays"].textValue()

            val secondResponse = chat.sendMessage(
                function(
                    functionName = functionCall.name,
                    content = result.textValue()
                )
            )
            println("Response:${secondResponse.message.content}")
        } ?: println(message.content)
    }
}

fun chatOllama(
    functionRegistry: FunctionRegistry,
    model: String = "mistral",
    baseUrl: String = "http://localhost:11434/v1/",
): OpenAiCompatibleChat = OpenAiCompatibleChat(
    apiKey = "none",
    model = model,
    baseUrl = baseUrl,
    functionRegistry = functionRegistry,
    defaultEvaluator = { chatChoices -> chatChoices.first() }
)

fun chatOpenAI(
    apiKey: String,
    functionRegistry: FunctionRegistry? = null,
    model: String = "gpt-4o",
): Chat = OpenAiCompatibleChat(
    apiKey = apiKey,
    model = model,
    functionRegistry = functionRegistry,
    defaultEvaluator = { chatChoices -> chatChoices.first() }
)

fun buildFunctionRegistry(): FunctionRegistry = registry {
    register(CurrentWeatherFunction())
    register(WeatherForecastFunction())
}

