package com.github.frtu.kotlin.spring.slack.builder

import com.github.frtu.kotlin.spring.slack.core.SlackEventHandlerRegistry
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrowExactly
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import com.github.frtu.kotlin.spring.slack.mock.AppMockConfig

class SlackRegisterEventConfigTest {
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
                AppMockConfig::class.java,
                SlackRegisterEventConfig::class.java,
            )
            .run { context ->
                //--------------------------------------
                // 3. Validate
                //--------------------------------------
                val result = context.getBean(String::class.java)
                logger.debug("result:{}", result)
                result shouldBe "OK"

                val slackEventHandlerRegistry = context.getBean(SlackEventHandlerRegistry::class.java)
                logger.debug("slackEventHandlerRegistry:{}", slackEventHandlerRegistry)
                slackEventHandlerRegistry.shouldNotBeNull()
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
                "application.slack.${SlackRegisterEventConfig.CONFIG_PREFIX}.enabled=false",
            )
            .withUserConfiguration(
                AppMockConfig::class.java,
                SlackRegisterEventConfig::class.java,
            )
            .run { context ->
                //--------------------------------------
                // 3. Validate
                //--------------------------------------
                shouldThrowExactly<NoSuchBeanDefinitionException> {
                    context.getBean(String::class.java)
                    context.getBean(SlackEventHandlerRegistry::class.java)
                }
            }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}