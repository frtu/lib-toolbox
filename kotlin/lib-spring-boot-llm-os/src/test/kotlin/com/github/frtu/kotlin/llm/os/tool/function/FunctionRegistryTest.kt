package com.github.frtu.kotlin.llm.os.tool.function

import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import sample.tool.function.CurrentWeatherFunction

class FunctionRegistryTest {
    @Test
    fun generateJsonSchema() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        val function = CurrentWeatherFunction()
        val functionCanonicalName = function.id.value
        val functionDescription = function.description

        val functionRegistry = FunctionRegistry()
        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        functionRegistry.register(function)
        val result = functionRegistry.getRegistry()
        logger.debug("result:$result")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        with(result) {
            shouldNotBeNull()
            size shouldBe 1
            with(result[0]) {
                name shouldBe functionCanonicalName
                description shouldBe functionDescription
                parameters.shouldNotBeNull()
            }
        }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}
