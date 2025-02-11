package sample

import com.github.frtu.kotlin.spring.slack.config.SlackAutoConfigs
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Import
import sample.tool.SampleToolConfig

@SpringBootApplication
@Import(
    SampleToolConfig::class,
    // Should be auto loaded by META-INF
    SlackAutoConfigs::class,
)
class SpringSlackTestApplication

fun main(args: Array<String>) {
    try {
        SpringApplicationBuilder(SpringSlackTestApplication::class.java)
            .web(WebApplicationType.REACTIVE)
            .run(*args)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}