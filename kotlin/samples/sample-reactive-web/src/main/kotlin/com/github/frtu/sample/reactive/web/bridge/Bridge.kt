package com.github.frtu.sample.reactive.web.bridge

import com.github.frtu.coroutine.webclient.SuspendableWebClient
import com.github.frtu.sample.reactive.web.controller.ResourceController
import org.springframework.stereotype.Component

@Component
class Bridge(private val suspendableWebClient: SuspendableWebClient) {
    suspend fun nonBlockingQuery() = suspendableWebClient.getBinary(ResourceController.PDF_PATH)
}