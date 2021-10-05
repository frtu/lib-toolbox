package com.github.frtu.kotlin.flow.core

import java.time.Instant
import java.util.UUID

data class Event(
    val id: UUID,
    val eventTimeEpochMilli: Long,
    val name: String,
    var description: String?,
) {
    companion object {
        fun event(description: String? = "Event description") = Event(
            id = UUID.randomUUID(),
            eventTimeEpochMilli = Instant.now().toEpochMilli(),
            name = "EventName",
            description = description,
        )
    }
}