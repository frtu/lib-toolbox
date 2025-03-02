package com.github.frtu.kotlin.ai.spring.config

import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrowExactly
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.boot.test.context.runner.ApplicationContextRunner

class LlmOsAutoConfigsTest {
    private val applicationContextRunner = ApplicationContextRunner()

    @Test
    fun `Build and initialize commands`() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        applicationContextRunner
            //--------------------------------------
            // 2. Execute
            //--------------------------------------
            .withPropertyValues(
                "application.${LlmOsAutoConfigs.CONFIG_PREFIX}.model=mistral",
            )
            .withUserConfiguration(
                LlmOsAutoConfigs::class.java,
            )
            .run { context ->
                //--------------------------------------
                // 3. Validate
                //--------------------------------------
                val result = context.getBean(ChatApiProperties::class.java)
                logger.debug("result:{}", result)
                //--------------------------------------
                // 3. Validate
                //--------------------------------------
                with(result) {
                    shouldNotBeNull()
                    logLevel shouldBe Level.DEBUG
                }
            }
    }

    @Test
    fun `Deactivate commands`() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        applicationContextRunner
            //--------------------------------------
            // 2. Execute
            //--------------------------------------
            .withPropertyValues(
                "application.${LlmOsAutoConfigs.CONFIG_PREFIX}.enabled=false",
            )
            .withUserConfiguration(
                LlmOsAutoConfigs::class.java,
            )
            .run { context ->
                //--------------------------------------
                // 3. Validate
                //--------------------------------------
                shouldThrowExactly<NoSuchBeanDefinitionException> {
                    context.getBean(ChatApiProperties::class.java)
                }
            }
    }


    @Test
    fun `Tune log level`() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        applicationContextRunner
            //--------------------------------------
            // 2. Execute
            //--------------------------------------
            .withPropertyValues(
                "application.${LlmOsAutoConfigs.CONFIG_PREFIX}.log-level=INFO",
                "application.${LlmOsAutoConfigs.CONFIG_PREFIX}.model=mistral",
            )
            .withUserConfiguration(
                LlmOsAutoConfigs::class.java,
            )
            .run { context ->
                val result = context.getBean(ChatApiProperties::class.java)
                logger.debug("result:{}", result)
                //--------------------------------------
                // 3. Validate
                //--------------------------------------
                with(result) {
                    shouldNotBeNull()
                    logLevel shouldBe Level.INFO
                }
            }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}