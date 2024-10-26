package com.github.frtu.kotlin.llm.os

import com.github.frtu.kotlin.llm.os.agent.UnstructuredBaseAgent
import com.github.frtu.kotlin.llm.os.tool.ToolRegistry
import sample.tool.function.CurrentWeatherFunction
import sample.tool.function.WeatherForecastFunction

suspend fun main() {
    val apiKey = "sk-xxx"

    val functionRegistry = buildFunctionRegistry()
    val toolRegistry = ToolRegistry(
        mutableMapOf(
            CurrentWeatherFunction.TOOL_NAME to CurrentWeatherFunction(),
            WeatherForecastFunction.TOOL_NAME to WeatherForecastFunction(),
        )
    )

    // === Choose between OpenAI & open source ===
    val chat = chatOpenAI(apiKey, toolRegistry)
//    val chat = chatOllama(functionRegistry)

    // === Start conversation ===
    val weatherAgent = UnstructuredBaseAgent(
        name = "Weather Agent",
        description = "You're an agent talking about weather",
        instructions = "Don't make assumptions about what values to plug into functions. Ask for clarification if a user request is ambiguous.",
        chat = chat,
        toolRegistry = functionRegistry,
        isStateful = true,
    )
    val response = weatherAgent.execute("What's the weather like in Glasgow, Scotland over the next 5 days?")
    println(response)
}
