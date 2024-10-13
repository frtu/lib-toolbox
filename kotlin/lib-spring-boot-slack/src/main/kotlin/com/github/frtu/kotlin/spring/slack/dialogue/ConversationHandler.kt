package com.github.frtu.kotlin.spring.slack.dialogue

import com.slack.api.model.event.Event
import kotlin.reflect.KClass

/**
 * Abstraction to only focus on conversation from and to an external Thread
 * @since 2.0.5
 */
interface ConversationHandler<E : Event> {
    /**
     * Listening to a particular event.
     *
     * @param eventId unique ID allowing to deduplicate event
     * @param event received & according payload
     * @param threadManager for advanced case, when you need to pull additional info or push complex case
     * @return Optional - for simple interaction when you only need to respond to answer to that event
     */
    fun invoke(
        message: MessageFromThread<E>,
        // Current event
        threadManager: ThreadManager,
    ): MessageToThread?

    /**
     * The event this handler is working with
     */
    fun getEvent(): KClass<E>
}