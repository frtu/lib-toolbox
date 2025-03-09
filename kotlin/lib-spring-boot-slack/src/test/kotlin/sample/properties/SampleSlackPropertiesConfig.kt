package sample.properties

import com.github.frtu.kotlin.spring.slack.config.SlackAppProperties
import com.github.frtu.kotlin.spring.slack.config.SlackProperties
import com.github.frtu.kotlin.spring.slack.config.SlackProperties.Companion.APP_NAME_DEFAULT
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SampleSlackPropertiesConfig {
    @Bean
    fun slackProperties(): SlackProperties {
        return SlackProperties(
            enabled = true,
            apps = mutableMapOf(
                APP_NAME_DEFAULT to slackAppProperties,
                "other" to slackAppProperties,
            ),
        )
    }

    companion object {
        val slackAppProperties = SlackAppProperties(
            token = "xapp-xxx",
            signingSecret = "yyy",
            botOauthToken = "xoxb-zzz",
        )
    }
}
