package sample.tool

import com.github.frtu.kotlin.tool.Tool
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SampleToolConfig {
    @Bean(IdentityTool.TOOL_NAME)
    fun identity(): Tool = IdentityTool()

    companion object {
        const val NUMBER_OF_TOOLS: Int = 1
    }
}