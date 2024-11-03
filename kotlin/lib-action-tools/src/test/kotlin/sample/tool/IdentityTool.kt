package sample.tool

import com.fasterxml.jackson.databind.JsonNode
import com.github.frtu.kotlin.action.management.ActionId
import com.github.frtu.kotlin.tool.ToolExecuter

class IdentityTool : ToolExecuter(
    id = ActionId(TOOL_NAME),
    description = "Return parameter as output",
    parameterJsonSchema = "{\"\$schema\":\"http://json-schema.org/draft-04/schema#\",\"title\":\"String\",\"type\":\"string\"}",
    returnJsonSchema = "{\"\$schema\":\"http://json-schema.org/draft-04/schema#\",\"title\":\"String\",\"type\":\"string\"}",
) {
    override suspend fun execute(parameter: JsonNode): JsonNode = parameter

    companion object {
        const val TOOL_NAME = "identity-function"
    }
}