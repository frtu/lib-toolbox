package com.github.frtu.kotlin.spring.slack.core

import com.slack.api.bolt.handler.builtin.SlashCommandHandler
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Repository

@Lazy
@Repository
class SlackCommandRegistry(
    private val registry: MutableMap<String, SlashCommandHandler> = mutableMapOf()
) {
    @PostConstruct
    fun init() {
        registry.entries.forEach { (beanName, handler) ->
            logger.info("Registered command /$beanName for type ${handler.javaClass}")
        }
    }

    fun getAll() = registry.entries

    operator fun get(name: String) = registry[name]

    private val logger = LoggerFactory.getLogger(this::class.java)
}