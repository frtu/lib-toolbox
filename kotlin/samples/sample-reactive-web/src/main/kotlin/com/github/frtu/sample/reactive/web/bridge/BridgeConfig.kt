package com.github.frtu.sample.reactive.web.bridge

import com.github.frtu.coroutine.webclient.SuspendableWebClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import reactor.util.retry.Retry

@Configuration
class BridgeConfig {
    @Value("\${server.port}")
    lateinit var serverPort: String

    @Bean
    fun suspendableWebClient(): SuspendableWebClient = SuspendableWebClient(
        WebClient.create("http://localhost:$serverPort"),
        Retry.max(1)
    )
}