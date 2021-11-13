package com.github.frtu.kotlin.patterns

import com.github.frtu.kotlin.flow.model.SampleFlow
import com.github.frtu.kotlin.patterns.spring.ErrorSampleFlow
import com.github.frtu.kotlin.patterns.spring.GoodSampleFlow
import com.github.frtu.kotlin.patterns.spring.SpringConfig
import com.github.frtu.kotlin.patterns.spring.SpringFlowRegistry
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.AnnotationConfigApplicationContext

internal class RegistryWithSpringTest {
    @Test
    fun `Register using applicationContext`() {
        val applicationContext = AnnotationConfigApplicationContext(SpringConfig::class.java)
        val springFlowRegistry = applicationContext.getBean(SpringFlowRegistry::class.java)

        val sampleFlow: SampleFlow = springFlowRegistry[GoodSampleFlow.FLOW_NAME]
        assertThat(sampleFlow.name).isEqualTo(GoodSampleFlow.FLOW_NAME)

        val errorSampleFlow: SampleFlow = springFlowRegistry[ErrorSampleFlow.FLOW_NAME]
        assertThat(errorSampleFlow.name).isEqualTo(ErrorSampleFlow.FLOW_NAME)
    }
}