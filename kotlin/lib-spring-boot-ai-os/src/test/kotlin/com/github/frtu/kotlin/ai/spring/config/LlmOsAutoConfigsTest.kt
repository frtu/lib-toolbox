package com.github.frtu.kotlin.ai.spring.config

import io.kotlintest.shouldThrowExactly
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
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
            .withUserConfiguration(
                LlmOsAutoConfigs::class.java,
            )
            .run { context ->
                //--------------------------------------
                // 3. Validate
                //--------------------------------------
                val result = context.getBean(ChatApiProperties::class.java)
                logger.debug("result:{}", result)
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

    private val logger = LoggerFactory.getLogger(this::class.java)
}