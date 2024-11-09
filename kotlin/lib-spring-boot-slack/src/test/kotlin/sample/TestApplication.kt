package sample

import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
class TestApplication

fun main(args: Array<String>) {
    SpringApplicationBuilder(TestApplication::class.java)
        .web(WebApplicationType.NONE)
        .run(*args)
}