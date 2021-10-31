package com.github.frtu.coroutine.webclient

import org.slf4j.LoggerFactory
import com.github.frtu.logs.core.RpcLogger
import com.github.frtu.logs.core.RpcLogger.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.reactivestreams.Publisher
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ReactiveHttpOutputMessage
import org.springframework.web.reactive.function.BodyExtractors
import org.springframework.web.reactive.function.BodyInserter
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.*
import reactor.core.publisher.Flux
import reactor.util.retry.Retry
import java.io.IOException
import java.io.PipedInputStream
import java.io.PipedOutputStream
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
open class SuspendableWebClient(
    private val webClient: WebClient,
    // Check https://projectreactor.io/docs/core/3.4.1/api/reactor/util/retry/Retry.html
    private val getRetrySpec: Retry = Retry.max(3),
) {
    companion object {
        fun create(baseUrl: String, builder: WebClient.Builder = WebClient.builder()): SuspendableWebClient =
            SuspendableWebClient(builder.baseUrl(baseUrl).build())

        val binariesMediaTypes = arrayOf(
            MediaType.APPLICATION_OCTET_STREAM,
            MediaType.APPLICATION_PDF,
            MediaType.IMAGE_PNG,
            MediaType.IMAGE_JPEG,
            MediaType.IMAGE_GIF
        )
    }

    /**
     * @param url full URL for the resource
     * @param requestId unique ID for post idempotency
     * @param headerPopulator header populator
     * @param responseConsumer response callback
     */
    fun get(
        url: String, requestId: UUID = UUID.randomUUID(),
        headerPopulator: Consumer<HttpHeaders> = Consumer { _ -> run {} }
    ): Flow<String> {
        val eventSignature = entries(client(), uri(url), requestId(requestId.toString()))!!
        try {
            rpcLogger.debug(eventSignature, phase("PREPARE TO SEND"))
            return webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .headers(headerPopulator)
                .retrieve()
                .bodyToFlux<String>()
                .retryWhen(getRetrySpec)
                .asFlow()
        } catch (e: WebClientResponseException) {
            // Don't log twice
            throw e
        } catch (e: Exception) {
            onException(eventSignature, e)
            throw e
        }
    }

    /**
     * @param url full URL for the resource
     * @param requestId unique ID for post idempotency
     * @param headerPopulator header populator
     * @param responseConsumer response callback
     */
    fun getBinary(
        url: String, requestId: UUID = UUID.randomUUID(),
        headerPopulator: Consumer<HttpHeaders> = Consumer { _ -> run {} },
        responseCallback: Consumer<WebClientResponse>? = null,
    ): Flow<DataBuffer> {
        val eventSignature = entries(client(), uri(url), requestId(requestId.toString()))!!
        try {
            rpcLogger.debug(eventSignature, phase("PREPARE TO SEND"))

            val spec = webClient.get()
                .uri(url)
                .accept(*binariesMediaTypes)
                .headers(headerPopulator)

            return spec
                .exchange()
                .flatMapMany { clientResponse ->
                    val statusCode = clientResponse.statusCode()
                    rpcLogger.debug(eventSignature, phase("ON_RESPONSE"), statusCode(statusCode.value()))

                    if (statusCode.is2xxSuccessful) {
                        rpcLogger.info(
                            eventSignature,
                            phase("SUCCESS"),
                            statusCode(statusCode.value())
                        )
                    } else {
                        rpcLogger.error(
                            eventSignature,
                            phase("FAILURE"),
                            statusCode(statusCode.value()),
                            errorMessage(statusCode.reasonPhrase)
                        )
                    }
                    // Response consumed, logged and cleaned up

                    // RETURN
//                    if (statusCode.isError) {
//                        throw clientResponse.createExceptionAndAwait()
//                    }

                    val osPipe = PipedOutputStream()
                    val isPipe = PipedInputStream(osPipe)

                    val body: Flux<DataBuffer> = clientResponse.body(BodyExtractors.toDataBuffers())
                        .doOnError { t ->
                            logger.error("Error reading body.", t)
                            // close pipe to force InputStream to error,
                            // otherwise the returned InputStream will hang forever if an error occurs
                            try {
                                isPipe.use {}
                            } catch (ioe: IOException) {
                                logger.error("Error closing streams", ioe)
                            }
                        }
                        .doFinally { s ->
                            try {
                                osPipe.use {}
                            } catch (ioe: IOException) {
                                logger.error("Error closing streams", ioe)
                            }
                        }
                    body
                }
//                .retrieve()
//                .bodyToFlux(DataBuffer::class.java)
                .asFlow()
        } catch (e: WebClientResponseException) {
            // Don't log twice
            throw e
        } catch (e: Exception) {
            onException(eventSignature, e)
            throw e
        }
    }

    /**
     * @param url full URL for the resource
     * @param requestId unique ID for post idempotency
     * @param requestBody post body object
     * @param headerPopulator header populator
     * @param responseConsumer response callback
     */
    suspend fun <T> post(
        url: String, requestId: UUID,
        requestBody: T,
        headerPopulator: Consumer<HttpHeaders> = Consumer { _ -> run {} },
        responseCallback: Consumer<WebClientResponse>? = null
    ): WebClientResponse {
        val eventSignature = entries(client(), uri(url), requestId(requestId.toString()))!!
        rpcLogger.debug(eventSignature, phase("PREPARE TO SEND"), requestBody(requestBody, false))

        val requestBodyInserters = BodyInserters.fromValue(requestBody)
        return post(url, requestId, requestBodyInserters, headerPopulator, responseCallback, eventSignature)
    }

    /**
     * @param url full URL for the resource
     * @param requestId unique ID for post idempotency
     * @param publisher reactive publisher
     * @param elementClass class of the produced object from publisher
     * @param headerPopulator header populator
     * @param responseConsumer response callback
     */
    suspend fun <T, P : Publisher<T>> post(
        url: String, requestId: UUID,
        publisher: P, elementClass: Class<T>,
        headerPopulator: Consumer<HttpHeaders> = Consumer { _ -> run {} },
        responseCallback: Consumer<WebClientResponse>? = null
    ): WebClientResponse {
        val eventSignature = entries(client(), uri(url), requestId(requestId.toString()))!!
        rpcLogger.debug(eventSignature, phase("PREPARE TO SEND"))

        return post(
            url,
            requestId,
            BodyInserters.fromPublisher(publisher, elementClass),
            headerPopulator,
            responseCallback,
            eventSignature
        )
    }

    /**
     * @param url full URL for the resource
     * @param requestId unique ID for post idempotency
     * @param requestBodyInserters BodyInserter for request body
     * @param headerPopulator header populator
     * @param responseConsumer response callback
     */
    suspend fun <T> post(
        url: String, requestId: UUID,
        requestBodyInserters: BodyInserter<T, ReactiveHttpOutputMessage>,
        headerPopulator: Consumer<HttpHeaders> = Consumer { _ -> run {} },
        responseCallback: Consumer<WebClientResponse>? = null,
        previousEventSignature: Array<out MutableMap.MutableEntry<Any?, Any?>>? = null
    ): WebClientResponse {
        val eventSignature = previousEventSignature ?: let {
            // ONLY log when not previously logged
            val newEventSignature = entries(client(), uri(url), requestId(requestId.toString()))!!
            rpcLogger.debug(newEventSignature, phase("PREPARE TO SEND"))
            newEventSignature
        }

        try {
            val spec = webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(headerPopulator)
                .body(requestBodyInserters)

            val webClientResult = spec
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
            rpcLogger.debug(eventSignature, statusCode(webClientResult.statusCode.value()), phase("FINISHED"))
            return webClientResult
        } catch (e: WebClientResponseException) {
            // Don't log twice
            throw e
        } catch (e: Exception) {
            onException(eventSignature, e)
            throw e
        }
    }

    fun onException(eventSignature: Array<out MutableMap.MutableEntry<Any?, Any?>>, e: Exception) {
        rpcLogger.error(eventSignature, errorMessage(e.message))
    }

    /** Logger for all inherited class */
    val logger = LoggerFactory.getLogger(this::class.java)
    val rpcLogger = RpcLogger.create(logger)
}
