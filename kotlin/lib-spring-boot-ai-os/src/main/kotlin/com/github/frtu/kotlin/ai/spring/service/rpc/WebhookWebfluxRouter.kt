package com.github.frtu.kotlin.ai.spring.service.rpc

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.frtu.kotlin.ai.os.tool.Tool
import com.github.frtu.kotlin.ai.os.tool.ToolRegistry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter
import org.springframework.web.reactive.function.server.json

@Configuration
@ConditionalOnClass(RouterFunction::class)
@ConditionalOnProperty(
    prefix = "application.ai.os.llm.endpoint", name = ["enabled"],
    havingValue = "true", matchIfMissing = true,
)
class WebhookWebfluxRouter(
    @Value("\${application.ai.os.llm.endpoint.url-prefix:$DEFAULT_URL_PREFIX}")
    private val urlPrefix: String = DEFAULT_URL_PREFIX,
) {
    @Autowired
    lateinit var toolRegistry: ToolRegistry

    companion object {
        const val DEFAULT_URL_PREFIX = "/v1/tools"
    }

    @Bean
    fun webhookRouter(): RouterFunction<ServerResponse> = coRouter {
        val toolNameKeyPath = "toolName"
        POST("$urlPrefix/{$toolNameKeyPath}") { serverRequest ->
            // Input parameters
            val toolName = serverRequest.pathVariable(toolNameKeyPath)
            val body = serverRequest.awaitBody(String::class)
            val toolParameter = jacksonObjectMapper().readValue(body, JsonNode::class.java)

            // Execution
            val tool: Tool = toolRegistry[toolName] ?: throw RuntimeException("$toolName doesn't exist")
            val result = tool.execute(toolParameter)

            // Result
            ok().json().bodyValueAndAwait(result)
        }
    }
}