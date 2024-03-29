package com.github.frtu.workflow.serverlessworkflow.trigger

import com.github.frtu.workflow.serverlessworkflow.DslBuilder
import com.github.frtu.workflow.serverlessworkflow.state.AbstractStateBuilder
import com.github.frtu.workflow.serverlessworkflow.trigger.EventTriggerBuilder.Companion.EVENT_TRIGGER_DEFAULT_NAME
import io.serverlessworkflow.api.events.EventDefinition
import io.serverlessworkflow.api.events.OnEvents
import io.serverlessworkflow.api.interfaces.State
import io.serverlessworkflow.api.states.DefaultState.Type.EVENT
import io.serverlessworkflow.api.states.EventState
import org.slf4j.LoggerFactory

/**
 * Builder for event trigger
 *
 * @author frtu
 * @since 1.2.5
 */
@DslBuilder
class EventTriggerBuilder(
    name: String = EVENT_TRIGGER_DEFAULT_NAME,
) : AbstractStateBuilder<EventState>(EventState(), EVENT, name) {
    private var eventType: String? = null

    @DslBuilder
    var type: String?
        get() = eventType
        set(value) {
            logger.trace("Event: type={}", value)
            model.withOnEvents(listOf(OnEvents().withEventRefs(listOf(value))))
            eventType = value
        }

    companion object {
        const val EVENT_TRIGGER_DEFAULT_NAME = "EventTrigger"

        fun getEventDefinition(state: EventState): List<EventDefinition> =
            state.onEvents.map {
                val eventType = it.eventRefs?.first()
                logger.trace("{}: name={}", "Event", eventType)
                EventDefinition()
                    .withName(eventType).withType(eventType)
                    .withKind(EventDefinition.Kind.CONSUMED)
                    .withDataOnly(false)
            }

        private val logger = LoggerFactory.getLogger(this::class.java)
    }
}

@DslBuilder
fun byEvent(
    type: String,
    name: String = EVENT_TRIGGER_DEFAULT_NAME,
    options: EventTriggerBuilder.() -> Unit = {}
): EventTrigger =
    EventTrigger(
        EventTriggerBuilder(name).apply {
            this.type = type
        }.apply(options).build()
    )

class EventTrigger(private val model: EventState) : Trigger<State>(TriggerCategory.BY_EVENT) {
    var type: String?
        get() = model.onEvents.first().eventRefs?.first()
        set(value) {
            model.withOnEvents(listOf(OnEvents().withEventRefs(listOf(value))))
        }

    override fun toResult(): List<State> = listOf(model)
}
