package com.github.frtu.kotlin.patterns.spring

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan
class SpringConfig {
    @Bean(value = ["${SpringFlowRegistry.SPRING_NAMESPACE + GoodSampleFlow.FLOW_NAME}"])
    fun goodSampleFlow() = GoodSampleFlow()

    @Bean("${SpringFlowRegistry.SPRING_NAMESPACE + ErrorSampleFlow.FLOW_NAME}")
    fun errorSampleFlow() = ErrorSampleFlow()
}