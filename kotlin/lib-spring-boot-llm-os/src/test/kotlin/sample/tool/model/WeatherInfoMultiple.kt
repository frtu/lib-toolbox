package sample.tool.model

data class WeatherInfoMultiple(
    val location: String,
    val unit: Unit,
    val numberOfDays: Int,
    val temperature: Int,
    val forecast: List<Int>,
) {
    constructor(
        location: String,
        unit: String,
        numberOfDays: Int,
        temperature: Int,
        forecast: List<Int>,
    ) : this(location, Unit.valueOf(unit), numberOfDays, temperature, forecast)
}
