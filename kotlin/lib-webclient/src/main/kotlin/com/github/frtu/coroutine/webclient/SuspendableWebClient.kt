package com.github.frtu.coroutine.webclient

import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.util.*

/**
 * Suspendable WebClient for coroutine. Open class that can be overridden.
 *
 * @see <a href="https://github.com/spring-projects/spring-framework/blob/main/src/docs/asciidoc/web/webflux-webclient.adoc">SpringFramework WebClient documentation</a>
 *
 * @author Frédéric TU
 * @since 1.1.1
 */
/**
 * @constructor webClient built and configured
 */
open class SuspendableWebClient(private val webClient: WebClient) {
    /**
     * @param url : full URL for the resource
     * @param requestId : unique ID for post idempotency
     * @param body : post body
     */
    suspend fun post(url: String, requestId: UUID, body: Any) {
        val responseBody = webClient.post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .retrieve()
            .awaitBody<String>()
        logger.info("""Call [${requestId}] succeeded ! Got response : ${responseBody}""")
    }

    /** Logger for all inherited class */
    val logger = LoggerFactory.getLogger(this::class.java)
}
