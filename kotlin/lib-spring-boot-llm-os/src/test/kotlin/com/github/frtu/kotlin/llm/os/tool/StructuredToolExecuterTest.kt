package com.github.frtu.kotlin.llm.os.tool

import com.github.frtu.kotlin.utils.io.toJsonNode
import com.github.frtu.kotlin.utils.io.toJsonString
import com.github.frtu.kotlin.utils.io.toObject
import io.kotlintest.matchers.collections.shouldNotBeEmpty
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import sample.tool.CurrentWeatherTool
import sample.tool.model.Unit
import sample.tool.model.WeatherForecastInputParameter
import sample.tool.model.WeatherInfoMultiple

class StructuredToolExecuterTest {
    @Test
    fun `Check flowName`() {
        val flowName = "Name1"
        val businessFlow = CurrentWeatherTool(flowName)
        businessFlow.name shouldBe flowName
    }

    @Test
    fun `Test doValidation`() {
        val location = "Glasgow, Scotland"
        val unit = Unit.celsius

        val parameter = WeatherForecastInputParameter(
            location = location,
            unit = unit,
            numberOfDays = 5,
        ).toJsonString().toJsonNode()

        runBlocking {
            val businessFlow = CurrentWeatherTool()
            val result = businessFlow.execute(parameter)
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
}
