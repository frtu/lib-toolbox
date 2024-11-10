package com.github.frtu.kotlin.spring.tool.config

import com.github.frtu.kotlin.spring.tool.SpringTool
import com.github.frtu.kotlin.spring.tool.scanner.ToolBuilderFromAnnotationScanner
import com.github.frtu.kotlin.spring.tool.source.rpc.WebhookWebfluxRouter
import com.github.frtu.kotlin.tool.ToolRegistry
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * Allow to bootstrap AI OS configuration
 * @author Frédéric TU
 * @since 2.0.9
 */
@Configuration
@Import(
    WebhookWebfluxRouter::class,
    ToolBuilderFromAnnotationScanner::class,
)
// If class exist scan packages
@ComponentScan(basePackageClasses = [SpringTool::class, ToolRegistry::class])
class SpringToolAutoConfigs