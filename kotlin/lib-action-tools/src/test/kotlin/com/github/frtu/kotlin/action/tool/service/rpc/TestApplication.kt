package com.github.frtu.kotlin.action.tool.service.rpc

import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Import
import sample.tool.SampleToolConfig

@SpringBootApplication
@Import(WebhookWebfluxRouter::class, SampleToolConfig::class)
class TestApplication

fun main(args: Array<String>) {
    try {
        SpringApplicationBuilder(TestApplication::class.java)
            .web(WebApplicationType.REACTIVE)
            .run(*args)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}