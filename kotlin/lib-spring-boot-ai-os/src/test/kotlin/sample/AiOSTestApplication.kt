package sample

import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
class AiOSTestApplication

fun main(args: Array<String>) {
    try {
        SpringApplicationBuilder(AiOSTestApplication::class.java)
            .web(WebApplicationType.NONE)
            .run(*args)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}