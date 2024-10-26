package com.github.frtu.kotlin.patterns

import com.github.frtu.kotlin.flow.core.SampleFlow
import com.github.frtu.kotlin.flow.core.SampleFlowRegistry
import io.kotlintest.shouldBe
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SampleFlowRegistryTest {
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

        eventRegistry[businessFlow.flowName] shouldBe businessFlow
        eventRegistry["${namePattern}-1"] shouldBe businessFlow1
        eventRegistry["${namePattern}-2"] shouldBe businessFlow2
    }
}