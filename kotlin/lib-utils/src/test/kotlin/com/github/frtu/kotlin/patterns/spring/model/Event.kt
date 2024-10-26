package com.github.frtu.kotlin.patterns.spring.model

import java.time.Instant
import java.util.UUID

/**
 * A Sample Event structure
 * @author Frédéric TU
 * @since 1.1.4
 */
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