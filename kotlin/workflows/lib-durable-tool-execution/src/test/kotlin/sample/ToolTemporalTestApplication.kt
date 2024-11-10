package sample

import com.github.frtu.kotlin.tool.execution.durable.spring.config.DurableAgentAutoConfigs
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Import
import sample.tool.SampleToolConfig

@SpringBootApplication
@Import(DurableAgentAutoConfigs::class, SampleToolConfig::class)
class ToolTemporalTestApplication

fun main(args: Array<String>) {
    try {
        SpringApplicationBuilder(ToolTemporalTestApplication::class.java)
            .web(WebApplicationType.REACTIVE)
            .run(*args)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}