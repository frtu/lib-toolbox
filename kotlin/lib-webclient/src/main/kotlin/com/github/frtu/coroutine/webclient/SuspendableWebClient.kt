package com.github.frtu.coroutine.webclient

import org.slf4j.LoggerFactory
import com.github.frtu.logs.core.RpcLogger
import com.github.frtu.logs.core.RpcLogger.*
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.*
import java.util.*
import java.util.function.Consumer

/**
 * Suspendable {@link WebClient} for coroutine. Open class that can be overridden.
 *
 * @see <a href="https://github.com/spring-projects/spring-framework/blob/main/src/docs/asciidoc/web/webflux-webclient.adoc">SpringFramework WebClient documentation</a>
 *
 * @author Frédéric TU
 * @since 1.1.1
 */
/**
 * @param webClient built and configured
 */
open class SuspendableWebClient(private val webClient: WebClient) {
    /**
     * @param url full URL for the resource
     * @param requestId unique ID for post idempotency
     * @param headerPopulator header populator
     * @param requestBody post body
     * @param responseConsumer response callback
     */
    suspend fun post(
        url: String, requestId: UUID,
        requestBody: Any,
        headerPopulator: Consumer<HttpHeaders> = Consumer { header -> {} },
        responseCallback: Consumer<WebClientResponse>? = null
    ) {
        val eventSignature = entries(client(), uri(url), requestId(requestId.toString()))
        try {
            rpcLogger.debug(eventSignature, phase("PREPARE TO SEND"), requestBody(requestBody, false))

            val webClientResult = webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(headerPopulator)
                .bodyValue(requestBody)
                .awaitExchange { clientResponse ->
                    val statusCode = clientResponse.statusCode()
                    rpcLogger.debug(eventSignature, phase("ON_RESPONSE"), statusCode(statusCode.value()))

                    val body = clientResponse.awaitBody<String>()
                    if (statusCode.is2xxSuccessful) {
                        rpcLogger.info(
                            eventSignature,
                            phase("SUCCESS"),
                            statusCode(statusCode.value()),
                            responseBody(body, false)
                        )
                    } else {
                        rpcLogger.error(
                            eventSignature,
                            phase("FAILURE"),
                            statusCode(statusCode.value()),
                            errorMessage(statusCode.reasonPhrase),
                            responseBody(body, false)
                        )
                    }
                    // Response consumed, logged and cleaned up

                    // CALLBACK IF NEEDED
                    val webClientResult = WebClientResponse(statusCode, body)
                    responseCallback?.accept(webClientResult)

                    // RETURN
                    if (statusCode.isError) {
                        throw clientResponse.createExceptionAndAwait()
                    }
                    webClientResult
                }
            rpcLogger.debug(eventSignature, phase("FINISHED"))
        } catch (e: Exception) {
            onException(eventSignature, e)
            throw e
        }
    }

    fun onException(eventSignature: Array<out MutableMap.MutableEntry<Any?, Any?>>?, e: Exception) {
        rpcLogger.error(eventSignature, errorMessage(e.message))
    }

    /** Logger for all inherited class */
    val logger = LoggerFactory.getLogger(this::class.java)
    val rpcLogger = RpcLogger.create(logger)
}
