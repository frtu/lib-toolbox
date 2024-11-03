package sample.tool

import com.github.frtu.kotlin.tool.Tool
import com.github.frtu.kotlin.tool.ToolRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import sample.tool.function.CurrentWeatherFunction
import sample.tool.function.WeatherForecastFunction

@Configuration
@ComponentScan(basePackageClasses = [ToolRegistry::class])
class SampleToolConfig {
    @Bean(CurrentWeatherFunction.TOOL_NAME)
    fun currentWeatherFunction(): Tool = CurrentWeatherFunction()

    @Bean(WeatherForecastFunction.TOOL_NAME)
    fun weatherForecastFunction(): Tool = WeatherForecastFunction()

    @Bean(IdentityTool.TOOL_NAME)
    fun identity(): Tool = IdentityTool()

    companion object {
        const val NUMBER_OF_TOOLS: Int = 3
    }
}