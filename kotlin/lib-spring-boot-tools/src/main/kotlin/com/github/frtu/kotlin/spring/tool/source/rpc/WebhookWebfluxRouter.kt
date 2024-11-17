package com.github.frtu.kotlin.spring.tool.source.rpc

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.frtu.kotlin.spring.tool.scanner.ToolBuilderFromAnnotationScanner
import com.github.frtu.kotlin.tool.Tool
import com.github.frtu.kotlin.tool.ToolRegistry
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Import
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter
import org.springframework.web.reactive.function.server.json

/**
 * Tool annotate an executable function
 *
 * @author Frédéric TU
 * @since 2.0.9
 */
@Configuration
@ConditionalOnClass(RouterFunction::class)
@ConditionalOnProperty(
    prefix = "application.tools.endpoint", name = ["enabled"],
    havingValue = "true", matchIfMissing = true,
)
@Import(ToolBuilderFromAnnotationScanner::class)
@DependsOn("annotatedBeans")
class WebhookWebfluxRouter(
    @Value("\${application.tools.endpoint.deployment-mode}")
    private val mode: DeploymentMode = DeploymentMode.STATIC,
    @Value("\${application.tools.endpoint.url-prefix}")
    private val urlPrefix: String = DEFAULT_URL_PREFIX,
) {
    @Autowired
    lateinit var toolRegistry: ToolRegistry

    companion object {
        const val DEFAULT_URL_PREFIX = "/v1/tools"
    }

    @Bean
    fun webhookRouter(): RouterFunction<ServerResponse> = coRouter {
        GET(urlPrefix) { serverRequest ->
            // Execution
            val result = toolRegistry.getAll().joinToString("\n") { "${it.id.value}|${it.description}" }

            // Result
            ok().json().bodyValueAndAwait(result)
        }
        when(mode) {
            DeploymentMode.STATIC -> {
                toolRegistry.getAll().forEach { tool ->
                    val toolNameKeyPath = tool.id.value
                    val path = "$urlPrefix/$toolNameKeyPath"
                    logger.info("Create one static endpoint path:$path")
                    POST(path) { serverRequest ->
                        // Input parameters
                        val body = serverRequest.awaitBody(String::class)
                        val toolParameter = jacksonObjectMapper().readValue(body, JsonNode::class.java)

                        // Execution
                        val result = tool.execute(toolParameter)

                        // Result
                        ok().json().bodyValueAndAwait(result)
                    }
                }
            }
            DeploymentMode.DYNAMIC -> {
                val toolNameKeyPath = "toolName"
                val path = "$urlPrefix/{$toolNameKeyPath}"
                logger.info("Create one generic endpoint path:$path")
                POST(path) { serverRequest ->
                    val toolName = serverRequest.pathVariable(toolNameKeyPath)
                    val tool: Tool = toolRegistry[toolName] ?: throw RuntimeException("$toolName doesn't exist")

                    // Input parameters
                    val body = serverRequest.awaitBody(String::class)
                    val toolParameter = jacksonObjectMapper().readValue(body, JsonNode::class.java)

                    // Execution
                    val result = tool.execute(toolParameter)

                    // Result
                    ok().json().bodyValueAndAwait(result)
                }
            }
        }
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}

enum class DeploymentMode {
    STATIC,
    DYNAMIC,
}