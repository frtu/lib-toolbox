package com.github.frtu.sample

import com.github.frtu.sample.persistence.basic.IEmailRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class Application {
    @Bean
    fun initializer(coroutineRepository: IEmailRepository): CommandLineRunner = CommandLineRunner {
        logger.debug("======================================")
    }

    internal val logger = LoggerFactory.getLogger(this::class.java)
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}