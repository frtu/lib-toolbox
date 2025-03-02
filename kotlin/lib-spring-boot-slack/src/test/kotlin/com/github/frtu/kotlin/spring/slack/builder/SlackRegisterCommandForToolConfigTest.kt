package com.github.frtu.kotlin.spring.slack.builder

import com.github.frtu.kotlin.tool.ToolRegistry
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrowExactly
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import com.github.frtu.kotlin.spring.slack.mock.AppMockConfig

class SlackRegisterCommandForToolConfigTest {
    private val applicationContextRunner = ApplicationContextRunner()

    @Test
    fun `Build and initialize tool commands`() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        applicationContextRunner
            //--------------------------------------
            // 2. Execute
            //--------------------------------------
            .withUserConfiguration(
                AppMockConfig::class.java,
                SlackRegisterCommandForToolConfig::class.java,
            )
            .run { context ->
                //--------------------------------------
                // 3. Validate
                //--------------------------------------
                val result = context.getBean(String::class.java)
                logger.debug("result:{}", result)
                result shouldBe "OK"

                val toolRegistry = context.getBean(ToolRegistry::class.java)
                logger.debug("toolRegistry:{}", toolRegistry)
                toolRegistry.shouldNotBeNull()
            }
    }

    @Test
    fun `Deactivate tool commands`() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        applicationContextRunner
            //--------------------------------------
            // 2. Execute
            //--------------------------------------
            .withPropertyValues(
                "application.slack.${SlackRegisterCommandForToolConfig.CONFIG_PREFIX}.enabled=false",
            )
            .withUserConfiguration(
                AppMockConfig::class.java,
                SlackRegisterCommandForToolConfig::class.java,
            )
            .run { context ->
                //--------------------------------------
                // 3. Validate
                //--------------------------------------
                shouldThrowExactly<NoSuchBeanDefinitionException> {
                    context.getBean(String::class.java)
                    // Dependency
                    context.getBean(ToolRegistry::class.java)
                }
            }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}