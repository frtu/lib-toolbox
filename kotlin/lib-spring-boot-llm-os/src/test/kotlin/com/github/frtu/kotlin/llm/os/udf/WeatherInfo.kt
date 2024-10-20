package com.github.frtu.kotlin.llm.os.udf

data class WeatherInfo(
    val location: String,
    val unit: Unit,
    val temperature: String,
    val forecast: List<String>,
) {
    constructor(
        location: String,
        unit: String,
        temperature: String,
        forecast: List<String>,
    ) : this(location, unit.toUnit(), temperature, forecast)
}

enum class Unit {
    celsius,
    fahrenheit,
}

fun String.toUnit(): Unit = try {
    Unit.valueOf(this)
} catch (e:  Exception) {
    println("Cannot convert $this, Error:${e.message}")
    throw e
}
