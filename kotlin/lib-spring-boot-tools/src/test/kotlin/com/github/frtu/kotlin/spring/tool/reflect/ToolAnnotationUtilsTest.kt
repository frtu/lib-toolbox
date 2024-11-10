package com.github.frtu.kotlin.spring.tool.reflect

import com.github.frtu.kotlin.spring.tool.annotation.WeatherToolAnnotation
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import sample.tool.model.WeatherForecastInputParameter
import sample.tool.model.WeatherInfoMultiple

class ToolAnnotationUtilsTest {
    @Test
    fun `Use toTool() to transform Method into Tool`() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        // Init var
        val method = WeatherToolAnnotation::class.java.methods.first { it.name == "forecast" }
        val targetObject = WeatherToolAnnotation()

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = method.toTool<WeatherForecastInputParameter, WeatherInfoMultiple>(targetObject)
        logger.debug("result:{}", result)

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        // Check metadata
        with(result) {
            shouldNotBeNull()
            id.value shouldBe "get_n_day_weather_forecast"
            description shouldBe "Get an N-day weather forecast"
            parameterJsonSchema.shouldNotBeNull()
            returnJsonSchema.shouldNotBeNull()
        }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}