package sample

import com.github.frtu.kotlin.spring.tool.source.rpc.WebhookWebfluxRouter
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Import
import sample.tool.SampleToolConfig

@SpringBootApplication
@Import(WebhookWebfluxRouter::class, SampleToolConfig::class)
class SpringToolTestApplication

fun main(args: Array<String>) {
    try {
        SpringApplicationBuilder(SpringToolTestApplication::class.java)
            .web(WebApplicationType.REACTIVE)
            .run(*args)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}