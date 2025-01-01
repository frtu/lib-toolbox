package com.github.frtu.kotlin.ai.feature.intent.model

/**
 * Represent an Intent LLM response giving the intent id and why LLm decide to classify user request to this intent.
 */
data class IntentResult(
    val intent: String,
    val reasoning: String,
)

