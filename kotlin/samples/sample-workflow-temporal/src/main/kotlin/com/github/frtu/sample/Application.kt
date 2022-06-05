package com.github.frtu.sample

import com.github.frtu.workflow.temporal.config.AutoConfigs
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(AutoConfigs::class)
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}