package sample.udf

import com.github.frtu.kotlin.spring.slack.event.AbstractEventHandler
import com.github.frtu.kotlin.spring.slack.event.MessageEventHandler
import com.github.frtu.kotlin.spring.slack.dialogue.ThreadManager
import com.github.frtu.logs.core.RpcLogger
import com.github.frtu.logs.core.RpcLogger.kind
import com.github.frtu.logs.core.RpcLogger.requestBody
import com.github.frtu.logs.core.RpcLogger.requestId
import com.slack.api.app_backend.events.payload.EventsApiPayload
import com.slack.api.bolt.context.builtin.EventContext
import com.slack.api.bolt.handler.BoltEventHandler
import com.slack.api.model.event.AppMentionEvent
import com.slack.api.model.event.MessageChannelJoinEvent
import com.slack.api.model.event.MessageEvent
import java.util.Map.entry
import org.slf4j.LoggerFactory
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
    fun appMentionEventHandler(): Pair<Class<AppMentionEvent>, BoltEventHandler<AppMentionEvent>> =
        object : AbstractEventHandler<AppMentionEvent>(AppMentionEvent::class.java) {
            override fun handleEvent(appMentionEvent: AppMentionEvent, eventId: String, ctx: EventContext) {
                with(appMentionEvent) {
                    logger.debug(
                        kind(type), requestId(eventId), entry("user", user),
                        entry("channel.id", channel), entry("event.ts", eventTs), requestBody(text)
                    )
                }
                with(
                    ThreadManager(ctx, appMentionEvent.threadTs
                    .takeIf { it != null } ?: appMentionEvent.ts)
                ) {
                    val reactionResponse = this.addReaction("eyes")
                    val messages = this.retrieveAllMessagesNonBot()
                    this.respond("I see ${messages.size} previous non bot messages")
                    this.removeReaction("eyes")

                    if (reactionResponse.isOk) {
                        ctx.ack() // Acknowledge the event
                    } else {
                        ctx.ackWithJson("{\"error\":\"Failed to add reaction\"}")
                    }
                }
            }
        }.toPair()

    @Bean
    fun newMember(): Pair<Class<MessageChannelJoinEvent>, BoltEventHandler<MessageChannelJoinEvent>> = Pair(
        MessageChannelJoinEvent::class.java,
        BoltEventHandler { req: EventsApiPayload<MessageChannelJoinEvent>, ctx: EventContext ->
            with(req.event) {
                // https://api.slack.com/apis/events-api#callback-field
                // {"kind":"channel_join","request_id":"Ev07PJTKRGF4","user":"U07PC8X2N94","channel.id":"C06500SQJTA",
                // "event.ts":"1727616269.059839","request":"<@U07PC8X2N94> has joined the channel"}
                logger.debug(
                    kind(subtype), requestId(req.eventId), entry("user", user),
                    entry("channel.id", channel), entry("event.ts", eventTs), requestBody(text)
                )
                // NULLABLE : , entry("team", team), entry("inviter", inviter)
            }
            ctx.say("Hi user ID:[${req.event.user}]!")
            ctx.ack()
        })

    private val logger = RpcLogger.create(LoggerFactory.getLogger(this::class.java))
}