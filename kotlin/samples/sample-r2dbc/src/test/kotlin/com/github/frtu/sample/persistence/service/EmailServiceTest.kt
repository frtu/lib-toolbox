package com.github.frtu.sample.persistence.service

import com.github.frtu.sample.persistence.r2dbc.json.EmailJson
import com.github.frtu.sample.persistence.r2dbc.json.EmailJsonDetail
import com.github.frtu.sample.persistence.r2dbc.json.entitytemplate.IEmailRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import java.util.*

@DisplayName("Test for EmailService")
@ExtendWith(MockKExtension::class)
internal class EmailServiceTest {
    @MockK
    lateinit var emailJsonRepository: IEmailJsonRepository

    private val givenEmail1 = EmailJson(EmailJsonDetail("rndfred@gmail.com", "Mail subject", "Lorem ipsum dolor sit amet.", "INIT"))
    private val givenEmail2 = EmailJson(EmailJsonDetail("rndfred@hotmail.com", "Mail subject", "Lorem ipsum dolor sit amet.", "SENT"))

    @Test
    fun findById() {
        // Fixture & mock
        coEvery {
            emailJsonRepository.findById(any())
        } returns givenEmail1

        // Execution
        val service = EmailService(emailJsonRepository)
        runBlocking {
            // Execute
            val result = service.findById(UUID.randomUUID())
            logger.debug("Test result: ${result}")

            // Validate
            Assertions.assertThat(result).isEqualTo(givenEmail1)
        }

        coVerify {
            emailJsonRepository.findById(any())
        }
    }

    @Test
    fun searchByQueryParams() {
        // Fixture & mock
        coEvery {
            emailJsonRepository.findAll(any())
        } returns Flux.just(givenEmail1, givenEmail2).asFlow()

        // Execution
        val service = EmailService(emailJsonRepository)
        runBlocking {
            // Execute
            val result = service.searchByQueryParams(mutableMapOf()).toList(mutableListOf())
            logger.debug("Test result: ${result}")

            // Validate
            Assertions.assertThat(result).isNotNull()
            Assertions.assertThat(result[0]).isEqualTo(givenEmail1)
            Assertions.assertThat(result[1]).isEqualTo(givenEmail2)
        }

        coVerify {
            emailJsonRepository.findAll(any())
        }
    }

    private val logger = LoggerFactory.getLogger(EmailServiceTest::class.java)
}