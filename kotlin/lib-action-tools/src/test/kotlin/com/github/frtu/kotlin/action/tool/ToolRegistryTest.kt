package com.github.frtu.kotlin.action.tool

import io.kotlintest.matchers.types.shouldBeNull
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import sample.tool.SampleToolConfig
import sample.tool.function.CurrentWeatherFunction
import sample.tool.function.WeatherForecastFunction

class ToolRegistryTest {
    private val applicationContextRunner = ApplicationContextRunner()

    @Test
    fun get() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        applicationContextRunner
            //--------------------------------------
            // 2. Execute
            //--------------------------------------
            .withUserConfiguration(
                SampleToolConfig::class.java
            )
            .run { context ->
                //--------------------------------------
                // 3. Validate
                //--------------------------------------
                val toolRegistry = context.getBean(ToolRegistry::class.java)
                toolRegistry.getAll().size shouldBe SampleToolConfig.NUMBER_OF_TOOLS
                toolRegistry[CurrentWeatherFunction.TOOL_NAME].shouldNotBeNull()
                toolRegistry[WeatherForecastFunction.TOOL_NAME].shouldNotBeNull()
            }
    }

    @Test
    fun split() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        applicationContextRunner
            //--------------------------------------
            // 2. Execute
            //--------------------------------------
            .withUserConfiguration(
                SampleToolConfig::class.java
            )
            .run { context ->
                //--------------------------------------
                // 3. Validate
                //--------------------------------------
                val toolRegistry = context.getBean(ToolRegistry::class.java)
                with(toolRegistry.split(listOf(CurrentWeatherFunction.TOOL_NAME))) {
                    this.getAll().size shouldBe 1
                    this[CurrentWeatherFunction.TOOL_NAME].shouldNotBeNull()
                    this[WeatherForecastFunction.TOOL_NAME].shouldBeNull()
                }
            }
    }
}
