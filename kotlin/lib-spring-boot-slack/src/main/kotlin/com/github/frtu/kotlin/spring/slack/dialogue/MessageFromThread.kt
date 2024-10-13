package com.github.frtu.kotlin.spring.slack.dialogue

import com.slack.api.model.event.Event

/**
 * Incoming message addressing from Thread
 * @since 2.0.5
 */
data class MessageFromThread<E : Event>(
    /** Message to bot */
    val message: E,
    /** Current event metadata (for idempotency) */
    val messageId: String,
)