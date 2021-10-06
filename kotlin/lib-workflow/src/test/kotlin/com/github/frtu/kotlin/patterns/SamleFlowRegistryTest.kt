package com.github.frtu.kotlin.patterns

import com.github.frtu.kotlin.flow.model.SampleFlow
import com.github.frtu.kotlin.flow.model.SampleFlowRegistry
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SamleFlowRegistryTest {
    @Test
    fun `Register and getElement`() {
        val namePattern = "flow-name"

        val flowName = "${namePattern}-default"
        val businessFlow = SampleFlow(flowName)
        val businessFlow1 = SampleFlow("${namePattern}-1")
        val businessFlow2 = SampleFlow("${namePattern}-2")


        val eventRegistry = SampleFlowRegistry(businessFlow)
        eventRegistry
            .register("${namePattern}-1", businessFlow1)
            .register("${namePattern}-2", businessFlow2)

        assertThat(eventRegistry.getElement(businessFlow.name)).isEqualTo(businessFlow)
        assertThat(eventRegistry.getElement("${namePattern}-1")).isEqualTo(businessFlow1)
        assertThat(eventRegistry.getElement("${namePattern}-2")).isEqualTo(businessFlow2)
    }
}