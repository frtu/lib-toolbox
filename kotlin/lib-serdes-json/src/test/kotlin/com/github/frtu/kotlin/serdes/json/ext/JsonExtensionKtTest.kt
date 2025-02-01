package com.github.frtu.kotlin.serdes.json.ext

import com.fasterxml.jackson.databind.node.NullNode
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.matchers.types.shouldBeNull
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import sample.model.EventInfo
import sample.model.Type
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

    @Test
    fun `Complex type conversion`() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        val id = UUID.randomUUID()
        val type = Type.ACCOUNT
        val eventTime = Instant.now()
        val bigDecimal = BigDecimal.valueOf(100_0010, 4)
        val metadata = mapOf(
            "test_traffic" to true,
            "priority" to 1.35F,
            "amount" to bigDecimal,
        )
        val tags = listOf("titi", "toto", "tata")
        val bean = EventInfo(
            id = id,
            type = type,
            eventTime = eventTime,
            data = null,
            data2 = NullNode.instance,
            metadata = metadata,
            tags = tags,
            tags2 = null,
        )

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val converted = bean.toJsonString(pretty = true)
        logger.debug("converted:$converted")
        // Check if Snake case is enforced
        converted shouldContain "event_time"

        val result = converted.toJsonObj(EventInfo::class.java)
        logger.debug("result:$result")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        with(result) {
            shouldNotBeNull()
            this.id shouldBe id
            this.type shouldBe type
            this.eventTime shouldBe eventTime
            this.data.shouldBeNull()
            this.data2 shouldBe NullNode.instance
            this.tags shouldBe tags
            this.tags2.shouldBeNull()
            this.defaultVal shouldBe "defaultValue"
            with(metadata) {
                get("test_traffic") shouldBe true
                get("priority") shouldBe 1.35F
                // Attention deserialization may return BigDecimal OR Double due to Any type erasure
                get("amount") shouldBe bigDecimal
            }
        }
    }

    @Test
    fun `Fault tolerant conversion for FAIL_ON_UNKNOWN_PROPERTIES`() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        val bigDecimal = BigDecimal.valueOf(100_0001, 4)
        val jsonString = """
        {
          "unknown-field" : "value",
          "id" : "a679a8f7-c062-48eb-ad36-9db69cc04802",
          "event_time" : "2025-01-30T06:15:04.418119Z",
          "data2" : null,
          "metadata" : {
            "test_traffic" : true,
            "priority" : 1.35,
            "amount" : $bigDecimal
          },
          "tags" : "titi",
          "tags2" : []
        }
        """.trimIndent()

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = jsonString.toJsonObj(EventInfo::class.java)
        logger.debug("result:$result")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        with(result) {
            shouldNotBeNull()
            this.id shouldBe id
            this.type.shouldBeNull()
            this.eventTime shouldBe eventTime
            this.data.shouldBeNull()
            this.data2 shouldBe NullNode.instance
            this.tags shouldBe listOf("titi")
            this.tags2 shouldBe emptyList()
            this.defaultVal shouldBe "defaultValue"
            with(metadata) {
                get("test_traffic") shouldBe true
                get("priority") shouldBe 1.35
                // Attention deserialization may return BigDecimal OR Double due to Any type erasure
                BigDecimal.valueOf(get("amount") as Double) shouldBe bigDecimal
            }
        }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}