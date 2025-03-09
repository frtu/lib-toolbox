package com.github.frtu.kotlin.spring.slack.builder

import com.github.frtu.kotlin.spring.slack.config.SlackProperties.Companion.APP_NAME_DEFAULT
import com.github.frtu.kotlin.spring.slack.core.SlackApp
import io.kotest.matchers.shouldBe
import io.kotlintest.matchers.types.shouldNotBeNull
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.getBeansOfType
import org.springframework.boot.test.context.runner.ApplicationContextRunner

class SlackInitAppConfigTest {
    private val applicationContextRunner = ApplicationContextRunner()

    @Test
    fun `Test SlackApp registration`() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        val otherAppName = "other"
        val appTokens = mapOf(
            APP_NAME_DEFAULT to "xapp-xxx",
            otherAppName to "xapp-yyy"
        )

        applicationContextRunner
            //--------------------------------------
            // 2. Execute
            //--------------------------------------
            .withPropertyValues(
                "application.slack.enabled=true",
                "application.slack.apps.$APP_NAME_DEFAULT.token=${appTokens[APP_NAME_DEFAULT]}",
                "application.slack.apps.$APP_NAME_DEFAULT.signing-secret=xxx",
                "application.slack.apps.$APP_NAME_DEFAULT.bot-oauth-token=xoxb-xxx",
                "application.slack.apps.$otherAppName.token=${appTokens[otherAppName]}",
                "application.slack.apps.$otherAppName.signing-secret=yyy",
                "application.slack.apps.$otherAppName.bot-oauth-token=xoxb-yyy",
            )
            .withUserConfiguration(
                SlackInitAppConfig::class.java,
            )
            .run { context ->
                //--------------------------------------
                // 3. Validate
                //--------------------------------------
                val slackAppMap = context.getBeansOfType<SlackApp>()

                slackAppMap.entries.forEach { entry ->
                    logger.debug("Found SlackApp name:{}", entry.key)
                    with(entry.value) {
                        shouldNotBeNull()
                        appToken() shouldBe appTokens[entry.key]
                    }
                }
            }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}
