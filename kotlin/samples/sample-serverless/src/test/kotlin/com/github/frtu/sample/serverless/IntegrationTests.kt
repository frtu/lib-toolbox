package com.github.frtu.sample.serverless

import io.cloudevents.CloudEvent
import io.cloudevents.core.builder.CloudEventBuilder
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import java.net.URI

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTests(@Autowired val rest: WebTestClient) {
    @Test
    fun echoWithCorrectHeaders() {
        val expectBody: WebTestClient.BodySpec<String, *> = rest.post()
            .uri("/foos")
            .header("ce-id", "12345") //
            .header("ce-specversion", "1.0") //
            .header("ce-type", "io.spring.event") //
            .header("ce-source", "https://spring.io/events") //
            .contentType(MediaType.APPLICATION_JSON) //
            .bodyValue("{\"value\":\"Dave\"}") //
            .exchange() //
            .expectStatus().isOk //
            .expectHeader().exists("ce-id") //
            .expectHeader().exists("ce-source") //
            .expectHeader().exists("ce-type") //
            .expectHeader().value("ce-id") { value: String -> check(value != "12345") } //
            .expectHeader().valueEquals("ce-type", "io.spring.event.Foo") //
            .expectHeader().valueEquals("ce-source", "https://spring.io/foos") //
            .expectBody(String::class.java)
//        expectBody.isEqualTo("{\"value\":\"Dave\"}")
    }

    @Test
    fun structuredRequestResponseEvents() {
        val expectBody: WebTestClient.BodySpec<String, *> = rest.post().uri("/event") //
            .contentType(MediaType("application", "cloudevents+json")) //
            .bodyValue(
                "{" //
                        + "\"id\":\"12345\"," //
                        + "\"specversion\":\"1.0\"," //
                        + "\"type\":\"io.spring.event\"," //
                        + "\"source\":\"https://spring.io/events\"," //
                        + "\"data\":{\"value\":\"Dave\"}}"
            ) //
            .exchange() //
            .expectStatus().isOk //
            .expectHeader().exists("ce-id") //
            .expectHeader().exists("ce-source") //
            .expectHeader().exists("ce-type") //
            .expectHeader().value("ce-id") { value: String -> check(value != "12345") } //
            .expectHeader().valueEquals("ce-type", "io.spring.event.Foo") //
            .expectHeader().valueEquals("ce-source", "https://spring.io/foos") //
            .expectBody(String::class.java)
//        expectBody.isEqualTo("{\"value\":\"Dave\"}")
    }

    @Test
    fun structuredRequestResponseCloudEventToString() {
        val expectBody: WebTestClient.BodySpec<String, *> = rest.post().uri("/event") //
            .bodyValue(
                CloudEventBuilder.v1() //
                    .withId("12345") //
                    .withType("io.spring.event") //
                    .withSource(URI.create("https://spring.io/events"))
                    .withData("{\"value\":\"Dave\"}".toByteArray()) //
                    .build()
            ) //
            .exchange() //
            .expectStatus().isOk //
            .expectHeader().exists("ce-id") //
            .expectHeader().exists("ce-source") //
            .expectHeader().exists("ce-type") //
            .expectHeader().value("ce-id") { value: String -> check(value != "12345") } //
            .expectHeader().valueEquals("ce-type", "io.spring.event.Foo") //
            .expectHeader().valueEquals("ce-source", "https://spring.io/foos") //
            .expectBody(String::class.java)
//        expectBody.isEqualTo("{\"value\":\"Dave\"}")
    }

    @Test
    fun structuredRequestResponseCloudEventToCloudEvent() {
        val expectBody: WebTestClient.BodySpec<CloudEvent, *> = rest.post().uri("/event") //
            .accept(MediaType("application", "cloudevents+json")) //
            .bodyValue(
                CloudEventBuilder.v1() //
                    .withId("12345") //
                    .withType("io.spring.event") //
                    .withSource(URI.create("https://spring.io/events")) //
                    .withData("{\"value\":\"Dave\"}".toByteArray()) //
                    .build()
            ) //
            .exchange() //
            .expectStatus().isOk //
            .expectHeader().exists("ce-id") //
            .expectHeader().exists("ce-source") //
            .expectHeader().exists("ce-type") //
            .expectHeader().value("ce-id") { value: String -> check(value != "12345") } //
            .expectHeader().valueEquals("ce-type", "io.spring.event.Foo") //
            .expectHeader().valueEquals("ce-source", "https://spring.io/foos") //
            .expectBody(CloudEvent::class.java)

//        expectBody.value<CloudEvent>(Consumer<CloudEvent> { event: CloudEvent ->
//            assertThat(String(event.data!!.toBytes())).isEqualTo("{\"value\":\"Dave\"}")
//        })
    }

    @Test
    fun requestResponseEvents() {
        val expectBody: WebTestClient.BodySpec<String, *> = rest.post().uri("/event").header("ce-id", "12345") //
            .header("ce-specversion", "1.0") //
            .header("ce-type", "io.spring.event") //
            .header("ce-source", "https://spring.io/events") //
            .contentType(MediaType.APPLICATION_JSON) //
            .bodyValue("{\"value\":\"Dave\"}") //
            .exchange() //
            .expectStatus().isOk //
            .expectHeader().exists("ce-id") //
            .expectHeader().exists("ce-source") //
            .expectHeader().exists("ce-type") //
            .expectHeader().value("ce-id") { value: String -> check(value != "12345") } //
            .expectHeader().valueEquals("ce-type", "io.spring.event.Foo") //
            .expectHeader().valueEquals("ce-source", "https://spring.io/foos") //
            .expectBody(String::class.java)
//        expectBody.isEqualTo("{\"value\":\"Dave\"}")
    }
}