package com.github.frtu.kotlin.llm.spring.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * Allow to bootstrap AI OS configuration
 */
@Configuration
@ComponentScan("com.github.frtu.kotlin.llm")
@Import(
    ChatApiConfigs::class,
)
class LlmOsAutoConfigs