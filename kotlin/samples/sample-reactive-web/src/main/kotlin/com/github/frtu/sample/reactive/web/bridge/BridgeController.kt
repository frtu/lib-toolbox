package com.github.frtu.sample.reactive.web.bridge

import com.github.frtu.sample.reactive.web.controller.ResourceController.Companion.PDF_PATH
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.server.bodyAndAwait
import reactor.core.publisher.Flux

/**
 * Mock a PDF service
 */
@RestController
//@RequestMapping("/pdf")
class BridgeController(bridge: Bridge) {
//    @GetMapping(produces = ["application/pdf"])
//    fun generatePdfGet(): ResponseEntity<DataBuffer> =
//        ok().contentType(MediaType.APPLICATION_PDF).bodyAndAwait(bridge.nonBlockingQuery())

}
