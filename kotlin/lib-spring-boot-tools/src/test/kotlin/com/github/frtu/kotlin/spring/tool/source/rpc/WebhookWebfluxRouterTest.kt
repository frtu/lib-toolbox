package com.github.frtu.kotlin.spring.tool.source.rpc

import com.github.frtu.kotlin.tool.Tool
import com.github.frtu.kotlin.tool.ToolRegistry
import com.github.frtu.kotlin.utils.io.toJsonString
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.reactive.server.WebTestClient
import sample.tool.CurrentWeatherTool
import sample.tool.SampleToolConfig
import sample.tool.model.WeatherForecastInputParameter

@Disabled
@Import(WebhookWebfluxRouter::class, SampleToolConfig::class)
@AutoConfigureWebTestClient(timeout = "60000") // 1 min for debugging
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
internal class WebhookWebfluxRouterTest(
    @Autowired private val client: WebTestClient,
    @Autowired private val toolRegistry: ToolRegistry,
) {
    private fun ToolRegistry.seed(vararg tools: Tool) = runBlocking {
        tools.forEach {
            toolRegistry[it.id] = it
        }
    }

    @Test
    fun route(): Unit = runBlocking {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        // Init var
        val location = "Glasgow, Scotland"
        val unit = sample.tool.model.Unit.celsius
        val parameter = WeatherForecastInputParameter(
            location = location,
            unit = unit,
            numberOfDays = 5,
        )

        // Init service
        val tool = CurrentWeatherTool()

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = client
            .post()
            .uri("${WebhookWebfluxRouter.DEFAULT_URL_PREFIX}/${tool.id.value}")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(parameter)
            .exchange()
            .expectStatus()
            .isOk
//            .expectBody<JsonNode>()

        logger.debug("result:${result.toJsonString()}")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
//        with(result) {
//            shouldNotBeNull()
//            value shouldBe name
//            size shouldBe 2
//        }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}