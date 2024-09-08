package com.github.frtu.sample.slack.udf

import com.github.frtu.kotlin.spring.slack.event.MessageEventHandler
import com.slack.api.app_backend.events.payload.EventsApiPayload
import com.slack.api.bolt.context.builtin.EventContext
import com.slack.api.bolt.handler.BoltEventHandler
import com.slack.api.model.event.AppMentionEvent
import com.slack.api.model.event.MessageEvent
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Registry for all Event type class
 * @see <a href="https://oss.sonatype.org/service/local/repositories/releases/archive/com/slack/api/slack-api-model/1.42.0/slack-api-model-1.42.0-javadoc.jar/!/com/slack/api/model/event/Event.html">All Event types</a>
 */
@Configuration
class EventHandlerFactory {
    @Bean
    fun messageEventHandler(): Pair<Class<MessageEvent>, BoltEventHandler<MessageEvent>> =
        MessageEventHandler().toPair()

    @Bean
    fun appMentionEventHandler(): Pair<Class<AppMentionEvent>, BoltEventHandler<AppMentionEvent>> = Pair(
        AppMentionEvent::class.java,
        BoltEventHandler { req: EventsApiPayload<AppMentionEvent>, ctx: EventContext ->
            ctx.say("Hi there!")
            ctx.ack()
        })

//    @Bean
//    fun appMentionEventHandler(): Pair<Class<AppMentionEvent>, BoltEventHandler<AppMentionEvent>> =
//        object : AbstractEventHandler<AppMentionEvent>(AppMentionEvent::class.java) {
//            override fun handleEvent(event: AppMentionEvent, eventId: String, ctx: EventContext) {
//                ctx.say("Hi there!")
//            }
//        }.toPair()
}