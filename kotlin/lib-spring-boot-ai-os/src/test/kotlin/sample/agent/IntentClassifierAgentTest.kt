package sample.agent

import com.github.frtu.kotlin.ai.os.llm.Chat
import com.github.frtu.kotlin.ai.spring.builder.ChatApiConfigs
import com.github.frtu.kotlin.test.containers.ollama.OllamaContainer
import io.kotlintest.matchers.types.shouldNotBeNull
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.testcontainers.junit.jupiter.Testcontainers

//@Disabled
@Testcontainers
internal class IntentClassifierAgentTest : OllamaContainer(
    // == Allows to use local Ollama for faster test
//    endpoint = "http://localhost:11434/v1/"
) {
    private lateinit var api: Chat

    @BeforeAll
    fun setup() {
        start()
        val model = "phi3:mini"
//        run(model = model)
        execInContainer("ollama", "run", model)
        api = ChatApiConfigs().chatOllama(
            model = model,
            baseUrl = "http://localhost:$mappedPortTemporal/v1/", // getBaseUrl()
        )
    }

    @Test
    fun `Test calling IntentClassifierAgent`(): Unit = runBlocking {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        // Init vars
        val request = "I want to know where my order is"

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val intentClassifierAgent = IntentClassifierAgent(api)
        val result = intentClassifierAgent.answer(request)
        logger.info("result:$result")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        with(result) {
            shouldNotBeNull()
        }
    }

    @AfterAll
    fun destroy() {
        stop()
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}
