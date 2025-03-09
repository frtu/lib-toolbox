package com.github.frtu.kotlin.spring.slack.builder

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration(proxyBeanMethods = false)
class SlackInitAppConfig {
    @Bean
    fun beanDefinitionRegistrar(environment: Environment): SlackAppRegistryPostProcessor {
        return SlackAppRegistryPostProcessor(environment)
    }
}