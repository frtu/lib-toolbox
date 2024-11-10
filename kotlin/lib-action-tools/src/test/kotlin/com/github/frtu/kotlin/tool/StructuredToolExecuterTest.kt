package com.github.frtu.kotlin.tool

import com.github.frtu.kotlin.utils.io.toJsonNode
import com.github.frtu.kotlin.utils.io.toJsonString
import com.github.frtu.kotlin.utils.io.toObject
import io.kotlintest.matchers.collections.shouldNotBeEmpty
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import kotlin.reflect.jvm.javaMethod
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import sample.tool.CurrentWeatherTool
import sample.tool.model.Unit
import sample.tool.model.WeatherForecastInputParameter
import sample.tool.model.WeatherInfoMultiple

class StructuredToolExecuterTest {
    @Test
    fun `Check flowName`() {
        val flowName = "Name1"
        val businessFlow = CurrentWeatherTool(flowName)
        businessFlow.id.value shouldBe flowName
    }

    @Test
    fun `Test execute using JsonNode object`() {
        val location = "Glasgow, Scotland"
        val unit = Unit.celsius

        val parameter = WeatherForecastInputParameter(
            location = location,
            unit = unit,
            numberOfDays = 5,
        ).toJsonString().toJsonNode()

        runBlocking {
            val tool = CurrentWeatherTool()
            val result = tool.execute(parameter)
            result.shouldNotBeNull()
            with(result.toObject(WeatherInfoMultiple::class.java)) {
                this.location shouldBe location
                this.unit shouldBe unit
                this.numberOfDays shouldBe numberOfDays
                this.temperature.shouldNotBeNull()
                this.forecast.shouldNotBeEmpty()
            }
        }
    }

    @Test
    fun `Test execute using concrete object`() {
        val location = "Glasgow, Scotland"
        val unit = Unit.celsius

        val parameter = WeatherForecastInputParameter(
            location = location,
            unit = unit,
            numberOfDays = 5,
        )

        runBlocking {
            val tool = CurrentWeatherTool()
            val result = tool.execute(parameter)
            result.shouldNotBeNull()
            with(result) {
                this.location shouldBe location
                this.unit shouldBe unit
                this.numberOfDays shouldBe numberOfDays
                this.temperature.shouldNotBeNull()
                this.forecast.shouldNotBeEmpty()
            }
        }
    }

    @Test
    fun `Test StructuredToolExecuter#create`() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        // Init var
        val location = "Glasgow, Scotland"
        val unit = Unit.celsius

        val parameter = WeatherForecastInputParameter(
            location = location,
            unit = unit,
            numberOfDays = 5,
        )

        // Init service
        val targetObject = CurrentWeatherTool()
        val executerMethod = CurrentWeatherTool::doExecute.javaMethod!!

        runBlocking {
            //--------------------------------------
            // 2. Execute
            //--------------------------------------
            val dynamicTool = StructuredToolExecuter.create<WeatherForecastInputParameter, WeatherInfoMultiple>(
                id = CurrentWeatherTool.TOOL_NAME,
                description = CurrentWeatherTool.TOOL_DESCRIPTION,
                executerMethod = executerMethod,
                targetObject = targetObject,
            )
            val result = dynamicTool.execute(parameter)
            logger.debug("result:$result")

            //--------------------------------------
            // 3. Validate
            //--------------------------------------
            result.shouldNotBeNull()
            with(result) {
                this.location shouldBe location
                this.unit shouldBe unit
                this.numberOfDays shouldBe numberOfDays
                this.temperature.shouldNotBeNull()
                this.forecast.shouldNotBeEmpty()
            }
        }
    }

    @Test
    fun `Test StructuredToolExecuter#create suspend function NOT SUPPORTED YET`() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        // Init var
        val location = "Glasgow, Scotland"
        val unit = Unit.celsius

        val parameter = WeatherForecastInputParameter(
            location = location,
            unit = unit,
            numberOfDays = 5,
        )

        // Init service
        val targetObject = CurrentWeatherTool()
        val executerMethod =
            CurrentWeatherTool::class.java.methods.first { it.name == "execute" }

        runBlocking {
            //--------------------------------------
            // 2. Execute
            //--------------------------------------
            val dynamicTool = StructuredToolExecuter.create<WeatherForecastInputParameter, WeatherInfoMultiple>(
                id = CurrentWeatherTool.TOOL_NAME,
                description = CurrentWeatherTool.TOOL_DESCRIPTION,
                executerMethod = executerMethod,
                targetObject = targetObject,
            )
            val result = shouldThrow<IllegalArgumentException> {
                dynamicTool.execute(parameter)
            }
            logger.debug("result:$result")

            //--------------------------------------
            // 3. Validate
            //--------------------------------------
            result.shouldNotBeNull()
            result.message shouldContain "wrong number of arguments: 1 expected: 2"
        }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}
