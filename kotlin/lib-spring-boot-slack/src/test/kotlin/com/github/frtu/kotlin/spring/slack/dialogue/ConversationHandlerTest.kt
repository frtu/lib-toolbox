package com.github.frtu.kotlin.spring.slack.dialogue

import com.slack.api.model.event.AppMentionEvent
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.util.UUID
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class ConversationHandlerTest {
    @Test
    fun `Simple implementation and returning message`() {
        //--------------------------------------
        // 1. Init
        //--------------------------------------
        val messageId = UUID.randomUUID().toString()
        val appMentionEvent = AppMentionEvent().apply {
            text = "text"
        }

        // Init service
        val threadManager = mockk<ThreadManager>(relaxed = true)
        val interactionHandlerSample = ConversationHandlerSample()

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val result = interactionHandlerSample.invoke(
            MessageFromThread(
                message = appMentionEvent,
                messageId = messageId,
            ), threadManager
        )
        logger.debug("result:$result")

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        interactionHandlerSample.getEvent() shouldBe AppMentionEvent::class
        with(result) {
            shouldNotBeNull()
            message shouldContain messageId
            message shouldContain appMentionEvent.text
        }
        every {
            threadManager.respond(any<MessageToThread>())
        }
    }

    class ConversationHandlerSample : ConversationHandler<AppMentionEvent> {
        override fun invoke(
            message: MessageFromThread<AppMentionEvent>,
            threadManager: ThreadManager,
        ): MessageToThread? {
            val messageToThread = MessageToThread(message = "${message.messageId}${message.message.text}")
            threadManager.respond(messageToThread)
            return messageToThread
        }

        override fun getEvent() = AppMentionEvent::class
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}