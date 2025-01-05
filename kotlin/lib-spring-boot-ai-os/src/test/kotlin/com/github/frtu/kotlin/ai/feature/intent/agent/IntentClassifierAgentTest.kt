package com.github.frtu.kotlin.ai.feature.intent.agent

import com.github.frtu.kotlin.ai.feature.intent.model.Intent
import com.github.frtu.kotlin.ai.feature.intent.model.IntentResult
import com.github.frtu.kotlin.ai.test.AbstractLocalAgentTest
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeEmpty
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@Disabled
class IntentClassifierAgentTest :
    AbstractLocalAgentTest<String, IntentResult>(
        {
            IntentClassifierAgent(
                chat = it,
                intents = listOf(
                    Intent(id = "Delivery status", description = "Inquiries about the current status of a delivery."),
                    Intent(
                        id = "Unblock delivery",
                        description = "Delivery is blocked and need to call API to unblock."
                    ),
                )
            )
        },
        "llama3",
    ) {

    @Test
    fun `Detect intent 'Delivery status'`() = benchmarkAcrossModel(
        "Hey, my command 12345678 should be delivered by Ninja Van. Can you help to check?",
    ) {
        with(it) {
            // LLM may mix upper & lower case
            intent.lowercase() shouldBe "Delivery status".lowercase()
            reasoning.shouldNotBeEmpty()
        }
    }

    @Test
    fun `Detect intent 'Unblock delivery'`() = benchmarkAcrossModel(
        "Hi, my delivery always get rejected. For the past several times, I asked but still nothing. Please help to resolve it.",
    ) {
        with(it) {
            // LLM may mix upper & lower case
            intent.lowercase() shouldBe "Unblock delivery".lowercase()
            reasoning.shouldNotBeEmpty()
        }
    }

    @Test
    fun `Detect intent 'Other'`() = benchmarkAcrossModel(
        "added an integration wto this channel.",
    ) {
        with(it) {
            // LLM may mix upper & lower case
            intent.lowercase() shouldBe "Other".lowercase()
            reasoning.shouldNotBeEmpty()
        }
    }
}