package com.github.frtu.coroutine.webclient

import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.slf4j.LoggerFactory

/**
 * Base class for MockWebServer test class
 *
 * @see <a href="https://www.baeldung.com/spring-mocking-webclient">Mocking a WebClient in Spring</a>
 *
 * @author Frédéric TU
 * @since 1.1.1
 */
open class BaseMockWebServerTest {
    internal var mockWebServer = MockWebServer()

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        logger.debug("=== ${this.javaClass.simpleName} starting mockWebServer:${mockWebServer}")

        mockWebServer.start()
    }

    @After
    fun tearDown() {
        logger.debug("=== ${this.javaClass.simpleName} stopping mockWebServer")
        mockWebServer.shutdown()
    }

    /** Logger for all inherited class */
    val logger = LoggerFactory.getLogger(this::class.java)
}