package com.github.frtu.kotlin.patterns

import com.github.frtu.kotlin.patterns.spring.model.Event
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class AbstractRegistryWithDefaultTest {
    @Test
    fun `Register default element using EventWithDefaultRegistry`() {
        val eventDefault = Event.event("default event")
        val eventRegistry = EventWithDefaultRegistry(eventDefault)
        Assertions.assertThat(eventRegistry["unknown"]).isEqualTo(eventDefault)
    }

    class EventWithDefaultRegistry(defaultElement: Event) :
        AbstractRegistryWithDefault<Event>(defaultElement, "event")
}