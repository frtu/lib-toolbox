package com.github.frtu.kotlin.ai.os

import com.github.frtu.kotlin.ai.os.llm.openai.protocol.toMessage
import com.github.frtu.kotlin.ai.os.memory.Conversation
import com.github.frtu.kotlin.ai.spring.builder.ChatApiConfigs
import com.github.frtu.kotlin.tool.ToolRegistry
import com.github.frtu.kotlin.tool.function.Function
import com.github.frtu.kotlin.tool.function.FunctionRegistry
import com.github.frtu.kotlin.tool.function.registry
import com.github.frtu.kotlin.utils.io.toJsonNode
import sample.tool.function.CurrentWeatherFunction
import sample.tool.function.WeatherForecastFunction

suspend fun main() {
    val apiKey = "sk-xxx"

    val functionRegistry = buildFunctionRegistry()
    val toolRegistry = ToolRegistry(
        listOf(CurrentWeatherFunction(), WeatherForecastFunction())
    )

    // === Choose between OpenAI & open source ===
    val chat = ChatApiConfigs().chatOpenAI(apiKey, toolRegistry)
//    val chat = ChatApiConfigs().chatOllama(functionRegistry)

    // === Start conversation ===
    with(Conversation()) {
        system("Don't make assumptions about what values to plug into functions. Ask for clarification if a user request is ambiguous.")

        chat.sendMessage(user("What's the weather like in Glasgow, Scotland over the next x days?"))
        val response = chat.sendMessage(user("5 days"))
        println(response)

        val message = response.message
        message.functionCall?.let { functionCall ->
            this.addResponse(message.toMessage())

            val functionToCall: Function<*, *> = functionRegistry[functionCall.name]!!

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

fun buildFunctionRegistry(): FunctionRegistry = registry {
    register(CurrentWeatherFunction())
    register(WeatherForecastFunction())
}

