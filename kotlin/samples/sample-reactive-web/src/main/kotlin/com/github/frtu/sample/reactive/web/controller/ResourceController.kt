package com.github.frtu.sample.reactive.web.controller

import com.github.frtu.sample.reactive.web.controller.ResourceController.Companion.PDF_PATH
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
@RequestMapping(PDF_PATH)
class ResourceController {
    @GetMapping(produces = ["application/pdf"])
    fun generatePdfGet(): Flux<DataBuffer> {
        val resource = DefaultResourceLoader().getResource("classpath:kotlin_docs.pdf")
        return DataBufferUtils.read(
            resource,
            DefaultDataBufferFactory(),
            4096
        )
    }

    companion object {
        const val PDF_PATH = "/resource/pdf"
    }
}
