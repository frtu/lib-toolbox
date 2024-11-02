package sample.tool

import com.github.frtu.kotlin.action.tool.Tool
import com.github.frtu.kotlin.action.tool.ToolRegistry
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
}