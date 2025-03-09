package com.github.frtu.kotlin.spring.slack.config

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import sample.properties.SampleSlackPropertiesConfig
import sample.properties.SampleSlackPropertiesConfig.Companion.slackAppProperties

class SlackPropertiesTest {
    private val applicationContextRunner = ApplicationContextRunner()

    @Test
    fun `Test defaultApp() using apps init config`() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        applicationContextRunner
            //--------------------------------------
            // 2. Execute
            //--------------------------------------
            .withUserConfiguration(
                SampleSlackPropertiesConfig::class.java,
            )
            .run { context ->
                //--------------------------------------
                // 3. Validate
                //--------------------------------------
                val result = context.getBean(SlackProperties::class.java)
                logger.debug("result:{}", result)

                with(result) {
                    enabled shouldBe true
                    // Though SlackProperties.app is null, it should be initialised
                    defaultApp() shouldBe slackAppProperties
                }
            }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}
