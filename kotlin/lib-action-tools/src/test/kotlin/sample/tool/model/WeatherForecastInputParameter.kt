package sample.tool.model

data class WeatherForecastInputParameter(
    val location: String,
    val unit: Unit,
    val numberOfDays: Int,
)