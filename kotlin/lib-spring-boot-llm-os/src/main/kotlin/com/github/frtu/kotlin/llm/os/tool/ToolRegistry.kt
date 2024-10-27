package com.github.frtu.kotlin.llm.os.tool

import com.github.frtu.kotlin.action.management.ActionRegistry
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
) : ActionRegistry<Tool>(registry)