package sample.agent.model

/**
 * Represent an Intent item (with an ID and the description, the LLM should use to return intent)
 */
data class Intent(
    val id: String,
    val description: String,
)
