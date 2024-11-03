package com.github.frtu.kotlin.serdes.json.ext

import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

import sample.model.Unit
import sample.model.WeatherInfo

class JsonExtensionKtTest {
    @Test
    fun `Conversion using JsonNode`() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        val location = "Glasgow, Scotland"
        val unit = Unit.celsius
        val temperature = "72"
        val bean = WeatherInfo(location, unit, temperature, listOf("sunny", "windy"))

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val converted = bean.objToJsonNode()
        logger.debug("converted:$converted")

        val result = converted.toJsonObj(WeatherInfo::class.java)
        logger.debug("result:$result")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        with(result) {
            shouldNotBeNull()
            this.location shouldBe location
            this.unit shouldBe unit
            this.temperature shouldBe temperature
        }
    }

    @Test
    fun `Conversion using String`() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        val location = "Glasgow, Scotland"
        val unit = Unit.celsius
        val temperature = "72"
        val bean = WeatherInfo(location, unit, temperature, listOf("sunny", "windy"))

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val converted = bean.toJsonString(pretty = true)
        logger.debug("converted:$converted")

        val result = converted.toJsonObj(WeatherInfo::class.java)
        logger.debug("result:$result")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        with(result) {
            shouldNotBeNull()
            this.location shouldBe location
            this.unit shouldBe unit
            this.temperature shouldBe temperature
        }
    }

    @Test
    fun `Between JsonNode and String`() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        val location = "Glasgow, Scotland"
        val unit = Unit.celsius
        val temperature = "72"

        val jsonNode = WeatherInfo(location, unit, temperature, listOf("sunny", "windy")).objToJsonNode()

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val converted = jsonNode.toJsonString(pretty = true)
        logger.debug("converted:$converted")

        val result = converted.toJsonNode()
        logger.debug("result:$result")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        with(result) {
            shouldNotBeNull()
            this["location"] shouldBe jsonNode["location"]
            this["unit"] shouldBe jsonNode["unit"]
            this["temperature"] shouldBe jsonNode["temperature"]
        }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}