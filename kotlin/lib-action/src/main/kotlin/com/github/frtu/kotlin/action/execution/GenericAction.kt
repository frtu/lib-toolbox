package com.github.frtu.kotlin.action.execution

import com.fasterxml.jackson.databind.JsonNode

/**
 * An function that can be executed using generic `JsonNode`
 *
 * @author Frédéric TU
 * @since 2.0.8
 */
interface GenericAction {
    suspend fun execute(parameter: JsonNode): JsonNode
}