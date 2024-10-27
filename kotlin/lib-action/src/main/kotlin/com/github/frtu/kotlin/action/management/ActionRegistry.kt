package com.github.frtu.kotlin.action.management

import javax.annotation.PostConstruct
import org.slf4j.LoggerFactory

/**
 * Registry of all `ActionMetadata` in the classpath
 *
 * @author Frédéric TU
 * @since 2.0.7
 */
open class ActionRegistry<ACTION : ActionMetadata>(
    registry: List<ACTION> = listOf()
) {
    /** registry key MUST be Tool name since this is what Function call will use */
    protected open val registry: MutableMap<ActionId, ACTION> = registry.associateBy { it.id }.toMutableMap()

    @PostConstruct
    fun init() {
        registry.entries.forEach { (key, tool) ->
            logger.info("Registered Tool key:[$key] with type ${tool.javaClass}")
        }
    }

    open operator fun get(key: ActionId) = registry[key]

    /** Convenience function as a getter */
    open operator fun get(key: String) = get(ActionId(key))

    open fun getAll() = registry.values

    open fun register(action: ACTION) = set(key = action.id, action = action)

    open operator fun set(key: ActionId, action: ACTION) {
        logger.debug(
            "Registering new action : key=[$key] id=[${action.id}] description=[${action.description}] " +
                    " parameterJsonSchema=[${action.parameterJsonSchema}]"
        )
        registry[key] = action
    }

    open fun split(keys: List<String>) = ActionRegistry(getAll().filter { keys.contains(it.id.value) })

    private val logger = LoggerFactory.getLogger(this::class.java)
}