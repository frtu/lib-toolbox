package com.github.frtu.kotlin.spring.tool.annotation

import sample.tool.model.WeatherForecastInputParameter
import sample.tool.model.WeatherInfoMultiple

@ToolGroup
class WeatherToolAnnotation {
    @Tool(
        id = "get_n_day_weather_forecast",
        description = "Get an N-day weather forecast",
    )
    fun forecast(request: WeatherForecastInputParameter): WeatherInfoMultiple = WeatherInfoMultiple(
        location = request.location,
        unit = request.unit,
        numberOfDays = request.numberOfDays,
        temperature = 30,
        forecast = listOf(31, 33, 34, 29),
    )
}
