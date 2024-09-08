package com.github.frtu.sample.slack

import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    SpringApplicationBuilder(Application::class.java)
        .web(WebApplicationType.NONE)
        .run(*args)
}