package com.github.frtu.kotlin.spring.tool.scanner

import com.github.frtu.kotlin.spring.tool.annotation.WeatherToolAnnotation
import io.kotlintest.matchers.collections.shouldNotBeEmpty
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import kotlin.reflect.jvm.javaMethod
import kotlinx.coroutines.runBlocking
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
        val executerMethod = WeatherToolAnnotation::forecast.javaMethod!!
        val targetObject = WeatherToolAnnotation()

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = executerMethod.toTool<WeatherForecastInputParameter, WeatherInfoMultiple>(targetObject)
        logger.debug("result:{}", result)

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        // Check metadata
        with(result) {
            shouldNotBeNull()
            id.value shouldBe WeatherToolAnnotation.TOOL_NAME
            description shouldBe WeatherToolAnnotation.TOOL_DESCRIPTION
            parameterJsonSchema.shouldNotBeNull()
            returnJsonSchema.shouldNotBeNull()
        }
    }

    @Test
    fun `Execute Annotated Method`(): Unit = runBlocking {
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
        val executerMethod = WeatherToolAnnotation::forecast.javaMethod!!
        val targetObject = WeatherToolAnnotation()

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val tool = executerMethod.toTool<WeatherForecastInputParameter, WeatherInfoMultiple>(targetObject)
        val result = tool.execute(parameter)
        logger.debug("result:{}", result)

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        // Check metadata
        with(result) {
            shouldNotBeNull()
            this.location shouldBe parameter.location
            this.unit shouldBe parameter.unit
            this.numberOfDays shouldBe parameter.numberOfDays
            this.temperature.shouldNotBeNull()
            this.forecast.shouldNotBeEmpty()
        }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}