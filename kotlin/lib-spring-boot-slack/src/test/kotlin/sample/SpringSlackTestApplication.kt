package sample

import com.github.frtu.kotlin.spring.slack.config.SlackAutoConfigs
import com.github.frtu.kotlin.spring.slack.config.SlackProperties
import com.github.frtu.kotlin.spring.slack.core.SlackApp
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import sample.tool.SampleToolConfig

@SpringBootApplication
@Import(
    SampleToolConfig::class,
    // Should be auto loaded by META-INF
    SlackAutoConfigs::class,
)
class SpringSlackTestApplication {
    @Bean
    fun initializer(slackApps: List<SlackApp>): CommandLineRunner = CommandLineRunner {
        logger.info("== SpringSlackTestApplication slackApps:{}", slackApps.map { it.name })
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}

fun main(args: Array<String>) {
    try {
        SpringApplicationBuilder(SpringSlackTestApplication::class.java)
            .web(WebApplicationType.REACTIVE)
            .run(*args)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}