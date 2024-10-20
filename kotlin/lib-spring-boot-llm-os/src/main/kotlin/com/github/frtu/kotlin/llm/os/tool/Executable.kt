package com.github.frtu.kotlin.llm.os.tool

import com.fasterxml.jackson.databind.JsonNode

/**
 * An object that can be executed using `JsonNode`
 *
 * @author Frédéric TU
 * @since 2.0.6
 */
interface Executable {
    suspend fun execute(parameter: JsonNode): JsonNode
}