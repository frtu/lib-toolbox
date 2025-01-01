package sample.tool

import com.github.frtu.kotlin.tool.ToolRegistry
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import sample.tool.function.CurrentWeatherFunction
import sample.tool.function.WeatherForecastFunction

class SampleToolConfigTest {
    private val applicationContextRunner = ApplicationContextRunner()

    @Test
    fun `Use as a base structure for your test`() {
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
                toolRegistry[IdentityTool.TOOL_NAME].shouldNotBeNull()
                toolRegistry[CurrentWeatherTool.TOOL_NAME].shouldNotBeNull()
                toolRegistry[CurrentWeatherFunction.TOOL_NAME].shouldNotBeNull()
                toolRegistry[WeatherForecastFunction.TOOL_NAME].shouldNotBeNull()
            }
    }
}