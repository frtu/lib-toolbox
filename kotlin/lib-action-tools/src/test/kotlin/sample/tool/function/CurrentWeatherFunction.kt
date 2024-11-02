package sample.tool.function

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.TextNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.frtu.kotlin.action.tool.function.Function
import kotlin.reflect.KFunction2
import org.slf4j.LoggerFactory
import sample.tool.model.WeatherInfo

class CurrentWeatherFunction(
    private val action: KFunction2<String, String, String> = ::currentWeather,
) : Function<WeatherInfo, String>(
    id = TOOL_NAME,
    description = "Get the current weather in a given location",
    parameterClass = WeatherInfo::class.java,
    String::class.java,
) {
    override suspend fun execute(parameter: JsonNode): JsonNode {
        val location = parameter["location"].textValue()
        val unit = parameter["unit"]?.textValue() ?: "fahrenheit"
        val result = action.invoke(location, unit)
        logger.info("Calling ${this::class} with parameter location:$location unit:$unit result:$result")
        return TextNode.valueOf(result)
    }

    companion object {
        const val TOOL_NAME = "get_current_weather"
    }
}

fun currentWeather(location: String, unit: String): String {
    val weatherInfo = WeatherInfo(location, unit, "72", listOf("sunny", "windy"))
    return jacksonObjectMapper().writeValueAsString(weatherInfo)
}