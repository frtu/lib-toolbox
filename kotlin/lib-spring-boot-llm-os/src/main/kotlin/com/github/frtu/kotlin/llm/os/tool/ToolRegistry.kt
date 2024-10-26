package com.github.frtu.kotlin.llm.os.tool

import javax.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class ToolRegistry(
    private val registry: MutableMap<String, Tool> = mutableMapOf()
) {
    @PostConstruct
    fun init() {
        registry.entries.forEach { (beanName, tool) ->
            logger.info("Registered Tool:[$beanName] with type ${tool.javaClass}")
        }
    }

    fun getAll() = registry.entries

    fun split(names: List<String>) = ToolRegistry(registry.filter { names.contains(it.key) }.toMutableMap())

    operator fun get(name: String) = registry[name]

    private val logger = LoggerFactory.getLogger(this::class.java)
}