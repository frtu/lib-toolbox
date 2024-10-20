package com.github.frtu.kotlin.llm.os

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.frtu.kotlin.llm.os.llm.Chat
import com.github.frtu.kotlin.llm.os.llm.openai.OpenAiCompatibleChat
import com.github.frtu.kotlin.llm.os.memory.Conversation
import com.github.frtu.kotlin.llm.os.tool.function.FunctionRegistry
import com.github.frtu.kotlin.llm.os.tool.function.registry
import com.github.frtu.kotlin.llm.os.udf.WeatherInfo
import com.github.frtu.kotlin.llm.os.udf.WeatherInfoMultiple
import kotlinx.serialization.json.jsonPrimitive

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

            val functionToCall = functionRegistry.getFunction(functionCall.name).action

            val functionArgs = functionCall.argumentsAsJson()
            val location = functionArgs.getValue("location").jsonPrimitive.content
            val unit = functionArgs["unit"]?.jsonPrimitive?.content ?: "fahrenheit"
            val numberOfDays = functionArgs.getValue("numberOfDays").jsonPrimitive.content

            val secondResponse = chat.sendMessage(
                function(
                    functionName = functionCall.name,
                    content = functionToCall(location, unit)
                )
            )
            println(secondResponse.message.content)
        } ?: println(message.content)
    }
}

private fun chatOllama(
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

private fun chatOpenAI(
    apiKey: String,
    functionRegistry: FunctionRegistry? = null,
    model: String = "gpt-4o",
): Chat = OpenAiCompatibleChat(
    apiKey = apiKey,
    model = model,
    functionRegistry = functionRegistry,
    defaultEvaluator = { chatChoices -> chatChoices.first() }
)

private fun buildFunctionRegistry(): FunctionRegistry = registry {
    function(
        name = "get_current_weather", description = "Get the current weather in a given location",
        kFunction2 = ::currentWeather, parameterClass = WeatherInfo::class.java, String::class.java,
    )
    function(
        name = "get_n_day_weather_forecast", description = "Get an N-day weather forecast",
        kFunction2 = ::currentWeather, parameterClass = WeatherInfoMultiple::class.java, String::class.java,
    )
}

fun currentWeather(location: String, unit: String): String {
    val weatherInfo = WeatherInfo(location, unit, "72", listOf("sunny", "windy"))
    return jacksonObjectMapper().writeValueAsString(weatherInfo)
}