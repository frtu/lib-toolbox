package com.github.frtu.kotlin.ai.feature.intent.model

/**
 * Represent an Intent item (with an ID and the description, the LLM should use to return intent)
 */
data class Intent(
    val id: String,
    val description: String,
)
