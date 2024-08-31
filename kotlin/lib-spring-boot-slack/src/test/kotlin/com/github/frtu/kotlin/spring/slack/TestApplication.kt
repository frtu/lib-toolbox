package com.github.frtu.kotlin.spring.slack

import com.github.frtu.kotlin.spring.slack.config.SlackConfig
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Import


@SpringBootApplication
@Import(SlackConfig::class)
class TestApplication

fun main(args: Array<String>) {
    SpringApplicationBuilder(TestApplication::class.java)
        .web(WebApplicationType.NONE)
        .run(*args)
}