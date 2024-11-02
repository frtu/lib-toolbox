package sample.tool

import com.fasterxml.jackson.databind.JsonNode
import com.github.frtu.kotlin.action.management.ActionId
import com.github.frtu.kotlin.ai.os.tool.ToolExecuter

class IdentityTool : ToolExecuter(
    id = ActionId("identity-function"),
    description = "Return parameter as output",
    parameterJsonSchema = "{\"\$schema\":\"http://json-schema.org/draft-04/schema#\",\"title\":\"String\",\"type\":\"string\"}",
    returnJsonSchema = "{\"\$schema\":\"http://json-schema.org/draft-04/schema#\",\"title\":\"String\",\"type\":\"string\"}",
) {
    override suspend fun execute(parameter: JsonNode): JsonNode = parameter
}