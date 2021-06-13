package com.github.frtu.persistence.service

import com.github.frtu.persistence.r2dbc.Email
import com.github.frtu.persistence.r2dbc.entitytemplate.EmailRepository
import com.github.frtu.persistence.r2dbc.entitytemplate.IEmailRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@DisplayName("Test for EmailService")
@ExtendWith(MockKExtension::class)
internal class EmailServiceTest {
    @MockK
    lateinit var emailRepository: IEmailRepository

    private val givenEmail1 = Email("rndfred@gmail.com", "Mail subject", "Lorem ipsum dolor sit amet.", "INIT", 1)
    private val givenEmail2 = Email("rndfred@hotmail.com", "Mail subject", "Lorem ipsum dolor sit amet.", "SENT", 2)

    @Test
    fun findById() {
        // Fixture & mock
        coEvery {
            emailRepository.findById(any())
        } returns givenEmail1

        // Execution
        val service = EmailService(emailRepository)
        runBlocking {
            // Execute
            val result = service.findById(1)
            LOGGER.debug("Test result: ${result}")

            // Validate
            Assertions.assertThat(result).isEqualTo(givenEmail1)
        }

        coVerify {
            emailRepository.findById(any())
        }
    }

    @Test
    fun searchByQueryParams() {
        // Fixture & mock
        coEvery {
            emailRepository.findAll(any())
        } returns Flux.just(givenEmail1, givenEmail2).asFlow()

        // Execution
        val service = EmailService(emailRepository)
        runBlocking {
            // Execute
            val result = service.searchByQueryParams(mutableMapOf()).toList(mutableListOf())
            LOGGER.debug("Test result: ${result}")

            // Validate
            Assertions.assertThat(result).isNotNull()
            Assertions.assertThat(result[0]).isEqualTo(givenEmail1)
            Assertions.assertThat(result[1]).isEqualTo(givenEmail2)
        }

        coVerify {
            emailRepository.findAll(any())
        }
    }

    private val LOGGER: Logger = LoggerFactory.getLogger(EmailServiceTest::class.java)
}