package sample.model

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.Instant
import java.util.UUID

/**
 * Multi type
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class EventInfo(
    val id: UUID,
    val type: Type?,
    val eventTime: Instant,
    val data: JsonNode?,
    val data2: JsonNode,
    val metadata: Map<String, Any>,
    val tags: List<String>,
    val tags2: List<String>?,
    val defaultVal: String = "defaultValue",
)

enum class Type {
    ACCOUNT,
    NOTIFICATION,
}