package com.github.frtu.sample.serverless.cloudevents.web

import io.cloudevents.CloudEvent
import io.cloudevents.core.builder.CloudEventBuilder
import io.cloudevents.spring.http.CloudEventHttpUtils
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.net.URI
import java.util.*

@RestController
class RestController {
    @PostMapping("/foos") // Let Spring do the type conversion of request and response body
    fun echo(
        @RequestBody foo: Foo,
        @RequestHeader headers: HttpHeaders
    ): ResponseEntity<Foo> {
        val attributes = CloudEventHttpUtils.fromHttp(headers)
            .withId(UUID.randomUUID().toString())
            .withSource(URI.create("https://spring.io/foos"))
            .withType("io.spring.event.Foo")
            .build()
        val outgoing = CloudEventHttpUtils.toHttp(attributes)
        return ResponseEntity.ok().headers(outgoing).body(foo)
    }

    @PostMapping("/event") // Use CloudEvent API and manual type conversion of request and response body
    fun event(@RequestBody body: Mono<CloudEvent>): Mono<CloudEvent> = body.map {
        CloudEventBuilder.from(it)
            .withId(UUID.randomUUID().toString())
            .withSource(URI.create("https://spring.io/foos"))
            .withType("io.spring.event.Foo")
            .withData(it.data!!.toBytes())
            .build()
    }
}

data class Foo(
    val value: String = UUID.randomUUID().toString()
)