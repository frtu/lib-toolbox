package com.github.frtu.kotlin.ai.os

import com.github.frtu.kotlin.ai.os.llm.agent.UnstructuredBaseAgent
import com.github.frtu.kotlin.ai.spring.builder.ChatApiConfigs
import com.github.frtu.kotlin.tool.ToolRegistry
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
    val weatherAgent = UnstructuredBaseAgent(
        id = "weather-agent",
        description = "You're an agent talking about weather",
        instructions = "Don't make assumptions about what values to plug into functions. Ask for clarification if a user request is ambiguous.",
        chat = chat,
        toolRegistry = toolRegistry,
        isStateful = true,
    )
    val response = weatherAgent.execute("What's the weather like in Glasgow, Scotland over the next 5 days?")
    println(response)
}
