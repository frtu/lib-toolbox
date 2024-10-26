package sample.tool

import com.fasterxml.jackson.databind.JsonNode
import com.github.frtu.kotlin.llm.os.tool.StructuredToolExecuter
import com.github.frtu.kotlin.utils.io.toJsonNode
import com.github.frtu.kotlin.utils.io.toJsonString
import sample.tool.model.WeatherForecastInputParameter
import sample.tool.model.WeatherInfoMultiple

/**
 * Sample tool returning JsonNode
 */
class CurrentWeatherTool(
    name: String = TOOL_NAME,
) : StructuredToolExecuter<WeatherForecastInputParameter, WeatherInfoMultiple>(
    name = name,
    description = "Get the current weather in a given location",
    parameterClass = WeatherForecastInputParameter::class.java,
    WeatherInfoMultiple::class.java,
) {
    override suspend fun execute(parameter: JsonNode): JsonNode {
        val location = parameter["location"].textValue()
        val unit: String = parameter["unit"]?.textValue() ?: "fahrenheit"
        val numberOfDays = parameter["numberOfDays"].intValue()
        return WeatherInfoMultiple(
            location = location,
            unit = unit,
            numberOfDays = numberOfDays,
            temperature = 30,
            forecast = listOf(31, 33, 34, 29),
        ).toJsonString().toJsonNode()
    }

    companion object {
        const val TOOL_NAME = "get_current_weather"
    }
}