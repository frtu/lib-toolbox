package com.github.frtu.kotlin.spring.slack.command

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.NullNode
import com.github.frtu.kotlin.translate.TextToJsonNodeTranslator
import com.github.frtu.kotlin.utils.io.toJsonNode

class UserInputToJsonNodeTranslator : TextToJsonNodeTranslator {
    override suspend fun execute(parameter: String): JsonNode {
        val args = parameter.split(" ")
        return args.findLast { it.isNotBlank() }?.replace("`", "")?.toJsonNode()
            ?: NullNode.instance
    }
}