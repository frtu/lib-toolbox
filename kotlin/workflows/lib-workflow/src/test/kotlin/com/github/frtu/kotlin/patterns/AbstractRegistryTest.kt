package com.github.frtu.kotlin.patterns

import com.github.frtu.kotlin.flow.model.Event
import com.github.frtu.kotlin.flow.model.Event.Companion.event
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class AbstractRegistryTest {
    @Test
    fun `Register and get`() {
        val namePattern = "event-name"
        val event1 = event("event for ${namePattern}-1")
        val event2 = event("event for ${namePattern}-2")

        val eventRegistry = EventRegistry()
        eventRegistry
            .register("${namePattern}-1", event1)
            .register("${namePattern}-2", event2)

        assertThat(eventRegistry["${namePattern}-2"]).isEqualTo(event2)
        assertThat(eventRegistry["${namePattern}-1"]).isEqualTo(event1)
    }

    @Test
    fun `Test doValidation negative case`() {
        assertThrows<UnrecognizedElementException> {
            EventRegistry().get("unknown")
        }
    }

    @Test
    fun `Get all elements`() {
        val namePattern = "event-name"
        val event1 = event("event for ${namePattern}-1")
        val event2 = event("event for ${namePattern}-2")

        val eventRegistry = EventRegistry()
        eventRegistry
            .register("${namePattern}-1", event1)
            .register("${namePattern}-2", event2)

        val all = eventRegistry.all()

        assertThat(all.size).isEqualTo(2)
        assertThat(all[0]).isEqualTo(event1)
        assertThat(all[1]).isEqualTo(event2)
    }

    class EventRegistry : AbstractRegistry<String, Event>("event") {
        public override fun register(name: String, element: Event) = super.register(name, element) as EventRegistry
    }
}
