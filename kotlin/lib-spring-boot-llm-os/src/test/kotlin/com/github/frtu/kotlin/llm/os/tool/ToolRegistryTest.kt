package com.github.frtu.kotlin.llm.os.tool

import com.github.frtu.kotlin.llm.os.tool.SampleToolConfig.Companion.TOOL_NAME_CURRENT_WEATHER
import com.github.frtu.kotlin.llm.os.tool.SampleToolConfig.Companion.TOOL_NAME_WEATHER_FORECAST
import io.kotlintest.matchers.types.shouldBeNull
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
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
                toolRegistry.getAll().size shouldBe 2
                toolRegistry[TOOL_NAME_CURRENT_WEATHER].shouldNotBeNull()
                toolRegistry[TOOL_NAME_WEATHER_FORECAST].shouldNotBeNull()
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
                with(toolRegistry.split(listOf(TOOL_NAME_CURRENT_WEATHER))) {
                    this.getAll().size shouldBe 1
                    this[TOOL_NAME_CURRENT_WEATHER].shouldNotBeNull()
                    this[TOOL_NAME_WEATHER_FORECAST].shouldBeNull()
                }
            }
    }
}

@Configuration
@ComponentScan(basePackageClasses = [ToolRegistry::class])
class SampleToolConfig {
    @Bean(TOOL_NAME_CURRENT_WEATHER)
    fun currentWeatherFunction(): Tool = CurrentWeatherFunction()

    @Bean(TOOL_NAME_WEATHER_FORECAST)
    fun weatherForecastFunction(): Tool = WeatherForecastFunction()

    companion object {
        const val TOOL_NAME_CURRENT_WEATHER = "currentWeatherFunction"
        const val TOOL_NAME_WEATHER_FORECAST = "weatherForecastFunction"
    }
}
