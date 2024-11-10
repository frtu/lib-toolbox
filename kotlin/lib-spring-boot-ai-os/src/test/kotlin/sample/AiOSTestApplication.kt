package sample

import com.github.frtu.kotlin.ai.spring.config.LlmOsAutoConfigs
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Import
import sample.tool.SampleToolConfig

@SpringBootApplication
@Import(
    SampleToolConfig::class,
    // Should be auto loaded by META-INF
    LlmOsAutoConfigs::class,
)
class AiOSTestApplication

fun main(args: Array<String>) {
    try {
        SpringApplicationBuilder(AiOSTestApplication::class.java)
            .web(WebApplicationType.REACTIVE)
            .run(*args)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}