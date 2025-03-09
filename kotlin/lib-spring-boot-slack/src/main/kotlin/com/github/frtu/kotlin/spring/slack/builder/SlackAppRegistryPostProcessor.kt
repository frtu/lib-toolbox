package com.github.frtu.kotlin.spring.slack.builder

import com.github.frtu.kotlin.spring.slack.config.SlackProperties
import com.github.frtu.kotlin.spring.slack.config.SlackProperties.Companion.SPRING_CONFIG_PREFIX
import com.github.frtu.kotlin.spring.slack.core.SlackApp
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.boot.context.properties.bind.Bindable
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.core.env.Environment

/**
 * Create SlackApp using SlackProperties read from binder SPRING_CONFIG_PREFIX
 * @since 2.0.18
 */
class SlackAppRegistryPostProcessor(
    environment: Environment,
) : BeanDefinitionRegistryPostProcessor {
    private val slackProperties: SlackProperties

    init {
        logger.debug("Found config $SPRING_CONFIG_PREFIX.enabled:{}", environment.getProperty("$SPRING_CONFIG_PREFIX.enabled"))
        val binder = Binder.get(environment)
        slackProperties = binder.bind(SPRING_CONFIG_PREFIX, Bindable.of(SlackProperties::class.java)).get()
        logger.debug("Found SlackAppProperties app names:{}", slackProperties.getRegistry().keys)
    }

    override fun postProcessBeanDefinitionRegistry(beanDefinitionRegistry: BeanDefinitionRegistry) {
        val slackRegistry = slackProperties.getRegistry()
        slackRegistry.entries.forEach { entry ->
            val builder = BeanDefinitionBuilder.genericBeanDefinition(SlackApp::class.java)
            builder.addConstructorArgValue(entry.value)
            logger.info("Registering SlackApp name:{}", entry.key)
            logger.trace("[SENSITIVE DATA - DO NOT LOG ON PROD] Found properties:{}", entry.value)
            beanDefinitionRegistry.registerBeanDefinition(
                entry.key,
                builder.getBeanDefinition()
            )
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}