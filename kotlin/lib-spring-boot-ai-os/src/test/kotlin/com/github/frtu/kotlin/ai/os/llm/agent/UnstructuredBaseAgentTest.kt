package com.github.frtu.kotlin.ai.os.llm.agent

import com.aallam.openai.api.chat.ChatChoice
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.FunctionCall
import com.aallam.openai.api.chat.TextContent
import com.aallam.openai.api.core.FinishReason
import com.aallam.openai.api.core.Role.Companion.Assistant
import com.github.frtu.kotlin.ai.os.instruction.PromptTemplate
import com.github.frtu.kotlin.tool.ToolRegistry
import com.github.frtu.kotlin.ai.os.llm.Chat
import com.github.frtu.kotlin.ai.os.llm.model.Answer
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import sample.tool.function.CurrentWeatherFunction
import sample.tool.function.WeatherForecastFunction

class UnstructuredBaseAgentTest {
    @Test
    fun execute(): Unit = runBlocking {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        val toolRegistry = ToolRegistry(
            listOf(CurrentWeatherFunction(), WeatherForecastFunction())
        )

        // Init service
        val chat = mockk<Chat>()

        coEvery { chat.sendMessage(any()) } returns Answer(
            ChatChoice(
                index = 0,
                message = ChatMessage(
                    role = Assistant,
                    messageContent = null,
                    name = null,
                    functionCall = FunctionCall(
                        nameOrNull = "get_n_day_weather_forecast",
                        argumentsOrNull = """
                        { "location":"Glasgow, Scotland", "unit":"celsius", "numberOfDays":5, "temperature":"", "forecast":[] }   
                        """.trimIndent()
                    ),
                    toolCalls = null,
                    toolCallId = null,
                    contentFilterResults = null,
                    contentFilterOffsets = null
                ),
                finishReason = FinishReason(value = "function_call"),
                logprobs = null
            )
        ) andThen Answer(
            ChatChoice(
                index = 0,
                message = ChatMessage(
                    role = Assistant,
                    messageContent = TextContent(
                        """
                            The weather forecast for Glasgow, Scotland over the next 5 days is as follows:
                
                            - **Day 1:** Sunny
                            - **Day 2:** Windy
                
                            Please note that there might be more detailed information for the upcoming days, but this is the current forecast overview.
                            The weather forecast for Glasgow, Scotland over the next 5 days is as follows:
                
                            - **Day 1:** Sunny
                            - **Day 2:** Windy
                
                            Please note that there might be more detailed information for the upcoming days, but this is the current forecast overview.
                        """.trimIndent()
                    ),
                    name = null,
                    functionCall = null,
                    toolCalls = null,
                    toolCallId = null,
                    contentFilterResults = null,
                    contentFilterOffsets = null
                ),
                finishReason = FinishReason("stop"),
                logprobs = null
            )
        )

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val weatherAgent = UnstructuredBaseAgent(
            id = "Weather Agent",
            description = "You're an agent talking about weather",
            instructions = "Don't make assumptions about what values to plug into functions. Ask for clarification if a user request is ambiguous.",
            chat = chat,
            toolRegistry = toolRegistry,
            isStateful = true,
        )
        val result = weatherAgent.execute("What's the weather like in Glasgow, Scotland over the next 5 days?")
        logger.debug("result:$result")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        with(result) {
            shouldNotBeNull()
        }
    }

    @Test
    fun `Create UnstructuredBaseAgent from PromptTemplate`(): Unit = runBlocking {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        val prompt = PromptTemplate(
            id = "prompt-generation-agent",
            description = "A meta-prompt Agent instructs the model to create a good prompt based on your task description or improve an existing one",
            template = """
                You're a prompt engineer, write a very bespoke, detailed and succinct prompt, that will generate an Cartoon storyboard writer optimized to write a story for my 2 pages cartoon content called UFO of a cute space cat and a police dog chasing him.
                
                Instructions
                - output the prompt you generate in markdown using variables in double curly brackets
                - output the prompt in a codeblock
                """.trimIndent(),
        )
        val chat = mockk<Chat>()

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = UnstructuredBaseAgent.create(chat, prompt)
        logger.debug("result:$result")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        with(result) {
            shouldNotBeNull()
            id.value shouldBe prompt.id
            description shouldBe prompt.description
            instructions shouldBe prompt.template
        }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}
