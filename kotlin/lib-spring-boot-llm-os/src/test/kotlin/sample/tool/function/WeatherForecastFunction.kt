package sample.tool.function

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.TextNode
import com.github.frtu.kotlin.llm.os.tool.function.Function
import kotlin.reflect.KFunction2
import sample.tool.function.model.WeatherInfoMultiple

class WeatherForecastFunction(
    private val action: KFunction2<String, String, String> = ::currentWeather,
): Function(
    name = "get_n_day_weather_forecast",
    description = "Get an N-day weather forecast",
    parameterClass = WeatherInfoMultiple::class.java,
    String::class.java,
) {
    override suspend fun execute(parameter: JsonNode): JsonNode {
        val location = parameter["location"].textValue()
        val unit = parameter["unit"]?.textValue() ?: "fahrenheit"
        val result = action.invoke(location, unit)
        return TextNode.valueOf(result)
    }

    companion object {
        const val TOOL_NAME = "weatherForecastFunction"
    }
}