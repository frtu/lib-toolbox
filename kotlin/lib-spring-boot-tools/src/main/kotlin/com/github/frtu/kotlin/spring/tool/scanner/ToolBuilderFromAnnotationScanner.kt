package com.github.frtu.kotlin.spring.tool.scanner

import com.github.frtu.kotlin.spring.tool.annotation.ToolGroup
import com.github.frtu.kotlin.tool.Tool
import com.github.frtu.kotlin.tool.ToolRegistry
import org.slf4j.LoggerFactory
import org.springframework.aop.framework.Advised
import org.springframework.aop.support.AopUtils
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Allow to build Tool from @Tool annotated method on @ToolGroup Bean
 *
 * @author Frédéric TU
 * @since 2.0.9
 */
@Configuration
@ConditionalOnProperty(
    prefix = "application.tools.scan", name = ["enabled"],
    havingValue = "true", matchIfMissing = true,
)
class ToolBuilderFromAnnotationScanner(
    private val applicationContext: ApplicationContext,
    private val toolRegistry: ToolRegistry,
) {
    @Bean
    fun annotatedBeans(): List<Tool> = applicationContext
        .getBeansWithAnnotation(ToolGroup::class.java)
        .flatMap { buildToolFromAnnotatedMethod(it.value) }
        .also { tools ->
            logger.info("Built tools size:{} names:[{}]", tools.size, tools.joinToString("|") { it.id.value })
            tools.forEach { toolRegistry.register(it) }
        }

    fun buildToolFromAnnotatedMethod(beansWithAnnotation: Any): List<Tool> {
        val target = (beansWithAnnotation.takeIf {
            // Check if bean is wrapped with AOP
            AopUtils.isAopProxy(it) && it is Advised
        } as? Advised)
            ?.targetSource?.target // Unbox if needed
            ?: beansWithAnnotation // Else use original bean

        logger.debug("Found annotated bean @ToolGroup type:[${target.javaClass}]")
        return target.javaClass.methods
            .filter {
                // Select only annotated method
                it.getAnnotation(com.github.frtu.kotlin.spring.tool.annotation.Tool::class.java) != null
            }
            .map {
                logger.debug("Found annotated method @Tool name:[${it.name}]")
                it.toTool<Any, Any>(target)
            } // Transform to Tool
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}
