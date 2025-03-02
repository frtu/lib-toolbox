package com.github.frtu.kotlin.spring.slack.builder

import com.github.frtu.kotlin.spring.slack.core.SlackCommandRegistry
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrowExactly
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import com.github.frtu.kotlin.spring.slack.mock.AppMockConfig

class SlackRegisterCommandConfigTest {
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
                SlackRegisterCommandConfig::class.java,
            )
            .run { context ->
                //--------------------------------------
                // 3. Validate
                //--------------------------------------
                val result = context.getBean(String::class.java)
                logger.debug("result:{}", result)
                result shouldBe "OK"

                val slackCommandRegistry = context.getBean(SlackCommandRegistry::class.java)
                logger.debug("slackCommandRegistry:{}", slackCommandRegistry)
                slackCommandRegistry.shouldNotBeNull()
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
                "application.slack.${SlackRegisterCommandConfig.CONFIG_PREFIX}.enabled=false",
            )
            .withUserConfiguration(
                AppMockConfig::class.java,
                SlackRegisterCommandConfig::class.java,
            )
            .run { context ->
                //--------------------------------------
                // 3. Validate
                //--------------------------------------
                shouldThrowExactly<NoSuchBeanDefinitionException> {
                    context.getBean(String::class.java)
                    context.getBean(SlackCommandRegistry::class.java)
                }
            }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}