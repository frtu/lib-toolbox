package sample.tool

import com.github.frtu.kotlin.tool.StructuredToolExecuter
import sample.tool.model.WeatherForecastInputParameter
import sample.tool.model.WeatherInfoMultiple

/**
 * Sample tool returning JsonNode
 */
class CurrentWeatherTool(
    id: String = TOOL_NAME,
) : StructuredToolExecuter<WeatherForecastInputParameter, WeatherInfoMultiple>(
    id = id,
    description = TOOL_DESCRIPTION,
    parameterClass = WeatherForecastInputParameter::class.java,
    returnClass = WeatherInfoMultiple::class.java,
) {
    fun doExecute(parameter: WeatherForecastInputParameter): WeatherInfoMultiple =
        WeatherInfoMultiple(
            location = parameter.location,
            unit = parameter.unit,
            numberOfDays = parameter.numberOfDays,
            temperature = 30,
            forecast = listOf(31, 33, 34, 29),
        )

    override suspend fun execute(parameter: WeatherForecastInputParameter): WeatherInfoMultiple = doExecute(parameter)

    companion object {
        const val TOOL_NAME = "get_current_weather_tool"
        const val TOOL_DESCRIPTION = "Get the current weather in a given location"
    }
}