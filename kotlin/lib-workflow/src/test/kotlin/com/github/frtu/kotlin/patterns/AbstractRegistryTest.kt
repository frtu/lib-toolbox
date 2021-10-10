package com.github.frtu.kotlin.patterns

import com.github.frtu.kotlin.flow.model.Event
import com.github.frtu.kotlin.flow.model.Event.Companion.event
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class AbstractRegistryTest {
    @Test
    fun `Register and getElement`() {
        val namePattern = "event-name"
        val event1 = event("event for ${namePattern}-1")
        val event2 = event("event for ${namePattern}-2")

        val eventRegistry = EventRegistry()
        eventRegistry
            .register("${namePattern}-1", event1)
            .register("${namePattern}-2", event2)

        assertThat(eventRegistry.getElement("${namePattern}-2")).isEqualTo(event2)
        assertThat(eventRegistry.getElement("${namePattern}-1")).isEqualTo(event1)
    }

    @Test
    fun `Test doValidation negative case`() {
        assertThrows<UnrecognizedElementException> {
            EventRegistry().getElement("unknown")
        }
    }

    @Test
    fun `Register default element`() {
        val eventDefault = event("default event")
        val eventRegistry = EventWithDefaultRegistry(eventDefault)
        assertThat(eventRegistry.getElement("unknown")).isEqualTo(eventDefault)
    }

    class EventRegistry : AbstractRegistry<String, Event>("event") {
        public override fun register(name: String, element: Event) = super.register(name, element) as EventRegistry
    }

    class EventWithDefaultRegistry(defaultElement: Event) :
        AbstractRegistry<String, Event>(
            "event", mutableMapOf(DEFAULT_KEY to defaultElement)
        ) {

        override fun getElement(name: String): Event = try {
            super.getElement(name)
        } catch (e: UnrecognizedElementException) {
            super.getElement(DEFAULT_KEY)
        }

        companion object {
            const val DEFAULT_KEY = "default"
        }
    }
}
