package sample.agent

import com.github.frtu.kotlin.ai.os.llm.Chat
import com.github.frtu.kotlin.ai.spring.builder.ChatApiConfigs
import com.github.frtu.kotlin.test.containers.ollama.OllamaContainer
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeEmpty
import io.kotlintest.matchers.types.shouldNotBeNull
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.testcontainers.junit.jupiter.Testcontainers
import sample.agent.model.Intent

@Disabled
class IntentClassifierAgentTest {
    private val chat: Chat = ChatApiConfigs().chatOllama(
        model = "llama3",
    )

    @Test
    fun `Detect intent 'Delivery status'`() = matchIntentWithQuery(
        "Delivery status", "Hey, my command 12345678 should be delivered by Ninja Van. Can you help to check?",
        chat,
    )

    @Test
    fun `Detect intent 'Unblock delivery'`() = matchIntentWithQuery(
        "Unblock delivery",
        "Hi, my delivery always get rejected. For the past several times, I asked but still nothing. Please help to resolve it.",
        chat,
    )

    @Test
    fun `Detect intent 'Other'`() = matchIntentWithQuery(
        "Other",
        "added an integration wto this channel.",
        chat,
    )

    companion object {
        internal fun matchIntentWithQuery(expectedIntent: String, userMessage: String, chat: Chat): Unit = runBlocking {
            //--------------------------------------
            // 1. Init
            //--------------------------------------
            // Init var
            val agent = IntentClassifierAgent(
                chat = chat,
                intents = listOf(
                    Intent(id = "Delivery status", description = "Inquiries about the current status of a delivery."),
                    Intent(id = "Unblock delivery", description = "Delivery is blocked and need to call API to unblock."),
                )
            )

            //--------------------------------------
            // 2. Execute
            //--------------------------------------
            val result = agent.execute(userMessage)
            logger.debug("result:{}", result)

            //--------------------------------------
            // 3. Validate
            //--------------------------------------
            with(result) {
                shouldNotBeNull()
                // LLM may mix upper & lower case
                intent.lowercase() shouldBe expectedIntent.lowercase()
                reasoning.shouldNotBeEmpty()
            }
        }

        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}