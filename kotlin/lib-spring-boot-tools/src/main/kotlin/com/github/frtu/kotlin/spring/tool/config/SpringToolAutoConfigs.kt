package com.github.frtu.kotlin.spring.tool.config

import com.github.frtu.kotlin.spring.tool.SpringTool
import com.github.frtu.kotlin.tool.ToolRegistry
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

/**
 * Allow to bootstrap AI OS configuration
 */
@Configuration
//@ConditionalOnProperty(
//    prefix = "application.tools.router", name = ["enabled"],
//    havingValue = "true", matchIfMissing = true,
//)
// If class exist scan packages
@ComponentScan(basePackageClasses = [SpringTool::class, ToolRegistry::class])
class SpringToolAutoConfigs