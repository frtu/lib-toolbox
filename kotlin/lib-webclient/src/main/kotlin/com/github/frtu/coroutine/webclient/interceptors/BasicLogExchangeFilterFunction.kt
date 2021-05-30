package com.github.frtu.coroutine.webclient.interceptors

import com.github.frtu.logs.core.RpcLogger
import com.github.frtu.logs.core.RpcLogger.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeFunction
import reactor.core.publisher.Mono

class BasicLogExchangeFilterFunction(logger: Logger = LoggerFactory.getLogger(BasicLogExchangeFilterFunction::class.java)) :
    ExchangeFilterFunction {
    private val rpcLogger = RpcLogger.create(logger)

    override fun filter(clientRequest: ClientRequest, next: ExchangeFunction): Mono<ClientResponse> {
        val entries = entries(
            client(),
            method(clientRequest.method().toString()),
            uri(clientRequest.url().toString())
        )
        rpcLogger.info(entries);
        return next.exchange(clientRequest);
    }
}