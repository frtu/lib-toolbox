package com.github.frtu.kotlin.flow.core

import java.util.UUID

data class Event(
    val id: UUID,
    val eventTimeEpochMilli: Long,
    val name: String,
    var description: String,
)