package com.github.frtu.persistence

import com.github.frtu.persistence.r2dbc.Email
import com.github.frtu.persistence.r2dbc.repository.IEmailRepository
import com.github.frtu.persistence.r2dbc.R2dbcConfiguration
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.*

@Configuration
@ComponentScan
@PropertySource("classpath:application.properties")
@Import(R2dbcConfiguration::class)
@SpringBootApplication
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