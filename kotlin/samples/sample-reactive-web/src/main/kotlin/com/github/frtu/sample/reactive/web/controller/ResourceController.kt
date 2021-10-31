package com.github.frtu.sample.reactive.web.controller

import org.springframework.core.io.DefaultResourceLoader
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

/**
 * Mock a PDF service
 */
@RestController
@RequestMapping("/resource")
class ResourceController {
    @GetMapping(value = ["/pdf"], produces = ["application/pdf"])
    fun generatePdf(): Flux<DataBuffer> {
        val resource = DefaultResourceLoader().getResource("classpath:kotlin_docs.pdf")
        return DataBufferUtils.read(
            resource,
            DefaultDataBufferFactory(),
            4096
        )
    }
}
