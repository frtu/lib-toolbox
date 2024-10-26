package com.github.frtu.kotlin.llm.os.tool

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

@Configuration
@ComponentScan(basePackageClasses = [ToolRegistry::class])
class SampleToolConfig {
    @Bean(CurrentWeatherFunction.TOOL_NAME)
    fun currentWeatherFunction(): Tool = CurrentWeatherFunction()

    @Bean(WeatherForecastFunction.TOOL_NAME)
    fun weatherForecastFunction(): Tool = WeatherForecastFunction()
}
