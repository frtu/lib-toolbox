package com.github.frtu.sample.persistence

import com.github.frtu.sample.persistence.r2dbc.basic.Email
import com.github.frtu.sample.persistence.r2dbc.basic.IEmailRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.AnnotationConfigApplicationContext

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