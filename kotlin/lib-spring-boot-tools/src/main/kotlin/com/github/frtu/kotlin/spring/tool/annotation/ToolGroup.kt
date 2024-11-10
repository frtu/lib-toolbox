package com.github.frtu.kotlin.spring.tool.annotation

import org.springframework.stereotype.Component

/**
 * ToolExecutor is an group of `Tool`
 *
 * @author Frédéric TU
 * @since 2.0.9
 */
@Component
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ToolGroup