package sample

import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
class SpringSlackTestApplication

fun main(args: Array<String>) {
    try {
        SpringApplicationBuilder(SpringSlackTestApplication::class.java)
            .web(WebApplicationType.NONE)
            .run(*args)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}