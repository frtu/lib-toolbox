package com.github.frtu.sample.persistence.r2dbc.json.entitytemplate

import com.github.frtu.persistence.exception.DataNotExist
import com.github.frtu.sample.persistence.r2dbc.json.EmailJson
import com.github.frtu.sample.persistence.r2dbc.json.EmailJsonDetail
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@DisplayName("Test for EmailRepository")
@ExtendWith(MockKExtension::class)
internal class EmailJsonRepositoryTest {
    @MockK
    lateinit var template: R2dbcEntityTemplate

    private val givenEmail1 = EmailJson(EmailJsonDetail("rndfred@gmail.com", "Mail subject", "Lorem ipsum dolor sit amet.", "INIT"))
    private val givenEmail2 = EmailJson(EmailJsonDetail("rndfred@hotmail.com", "Mail subject", "Lorem ipsum dolor sit amet.", "SENT"))

    @Test
    fun `Test basic findById`() {
        // Fixture & mock
        every {
            template.selectOne(any(), EmailJson::class.java)
        } returns Mono.just(givenEmail1)

        // Execution
        val repository = EmailJsonRepository(template)
        runBlocking {
            // Execute
            val result = repository.findById(UUID.randomUUID())
            LOGGER.debug("Test result: ${result}")

            // Validate
            assertThat(result).isEqualTo(givenEmail1)
        }
    }

    @Test
    fun `Test non existing findById raise exception`() {
        // Fixture & mock
        every {
            template.selectOne(any(), EmailJson::class.java)
        } returns Mono.empty()

        // Execution
        val repository = EmailJsonRepository(template)
        runBlocking {
            // Execute & Validate
            assertThrows<DataNotExist> {
                repository.findById(UUID.randomUUID())
            }
        }
    }

    @Test
    fun `Test basic findAll`() {
        // Fixture & mock
        every {
            template.select(EmailJson::class.java).all()
        } returns Flux.just(givenEmail1, givenEmail2)

        // Execution
        val repository = EmailJsonRepository(template)
        runBlocking {
            // Execute flow to list
            val result = repository.findAll().toList(mutableListOf())
            LOGGER.debug("Test result: ${result}")

            // Validate
            assertThat(result).isNotNull()
            assertThat(result[0]).isEqualTo(givenEmail1)
            assertThat(result[1]).isEqualTo(givenEmail2)
        }
    }

    @Test
    fun `Test findAll with searchParams`() {
        // Fixture & mock
        every {
            template.select(EmailJson::class.java).matching(any()).all()
        } returns Flux.just(givenEmail1, givenEmail2)

        // Execution
        val repository = EmailJsonRepository(template)
        runBlocking {
            // Execute flow to list
            val result = repository.findAll(mutableMapOf()).toList(mutableListOf())
            LOGGER.debug("Test result: ${result}")

            // Validate
            assertThat(result).isNotNull()
            assertThat(result[0]).isEqualTo(givenEmail1)
            assertThat(result[1]).isEqualTo(givenEmail2)
        }
    }

    private val LOGGER = LoggerFactory.getLogger(EmailJsonRepositoryTest::class.java)
}