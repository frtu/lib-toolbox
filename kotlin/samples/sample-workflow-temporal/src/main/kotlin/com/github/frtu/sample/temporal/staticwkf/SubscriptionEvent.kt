package com.github.frtu.sample.temporal.staticwkf

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant
import java.util.*

class SubscriptionEvent(
    @JsonProperty("data")
    val data: String,
    @JsonProperty("type")
    val type: String = "subscription",
    @JsonProperty("id")
    var id: UUID = UUID.randomUUID(),
    @JsonProperty("time_ms")
    val timeMillis: Long = Instant.now().toEpochMilli(),
    @JsonProperty("metadata")
    val metadata: Map<String, Any> = emptyMap(),
)