package com.github.frtu.kotlin.llm.os.tool

import javax.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

/**
 * Registry of all `Tool` in the classpath
 *
 * @author Frédéric TU
 * @since 2.0.7
 */
@Repository
class ToolRegistry(
    registry: List<Tool> = listOf()
) {
    /** registry key MUST be Tool name since this is what Function call will use */
    private val registry: MutableMap<String, Tool> = registry.associateBy { it.name }.toMutableMap()

    @PostConstruct
    fun init() {
        registry.entries.forEach { (key, tool) ->
            logger.info("Registered Tool key:[$key] with type ${tool.javaClass}")
        }
    }

    fun getAll() = registry.values

    fun split(names: List<String>) = ToolRegistry(getAll().filter { names.contains(it.name) })

    operator fun get(name: String) = registry[name]

    private val logger = LoggerFactory.getLogger(this::class.java)
}