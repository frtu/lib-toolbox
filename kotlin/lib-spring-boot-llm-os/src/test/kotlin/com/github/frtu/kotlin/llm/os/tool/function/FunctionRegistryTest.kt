package com.github.frtu.kotlin.llm.os.tool.function

import com.github.frtu.kotlin.llm.os.currentWeather
import com.github.frtu.kotlin.llm.os.udf.WeatherInfo
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class FunctionRegistryTest {
    @Test
    fun generateJsonSchema() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        val functionName = "currentWeather"
        val functionDescription = "Get the current weather in a given location"
        val parameterClass = WeatherInfo::class.java

        val functionRegistry = FunctionRegistry()
        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        functionRegistry.registerFunction(functionName, functionDescription, ::currentWeather, parameterClass, String::class.java)
        val result = functionRegistry.getRegistry()
        logger.debug("result:$result")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        with(result) {
            shouldNotBeNull()
            size shouldBe 1
            with(result[0]) {
                name shouldBe functionName
                description shouldBe functionDescription
                parameters.shouldNotBeNull()
            }
        }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}
