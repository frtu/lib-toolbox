package com.github.frtu.sample.persistence

import com.github.frtu.persistence.r2dbc.config.PostgresR2dbcConfiguration
import com.github.frtu.sample.persistence.r2dbc.Email
import com.github.frtu.sample.persistence.r2dbc.repository.IEmailRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.*

class AppConfig

fun main() {
    val LOGGER = LoggerFactory.getLogger(AppConfig::class.java)

    val context = AnnotationConfigApplicationContext(AppConfig::class.java)
    val repository: IEmailRepository = context.getBean(IEmailRepository::class.java)

    val entity = Email(
        "rndfred@gmail.com", "Mail subject",
        "Lorem ipsum dolor sit amet.", "SENT"
    )
    runBlocking {
        repository.save(entity)
        LOGGER.debug(repository.findAll().toList(mutableListOf()).toString())
    }
}