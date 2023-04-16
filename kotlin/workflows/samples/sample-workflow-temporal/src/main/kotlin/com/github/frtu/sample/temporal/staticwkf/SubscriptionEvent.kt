package com.github.frtu.sample.temporal.staticwkf

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.frtu.sample.pattern.VO
import java.time.Instant
import java.util.*

@VO
class SubscriptionEvent(
    @JsonProperty("email")
    val data: String?,
    @JsonProperty("type")
    val type: String = "subscription",
    @JsonProperty("id")
    var id: UUID = UUID.randomUUID(),
    @JsonProperty("time_ms")
    val timeMillis: Long = Instant.now().toEpochMilli(),
    @JsonProperty("metadata")
    val metadata: Map<String, Any> = emptyMap(),
)
