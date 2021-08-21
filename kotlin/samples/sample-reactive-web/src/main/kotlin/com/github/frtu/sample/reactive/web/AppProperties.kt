package com.github.frtu.sample.reactive.web

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("prefix")
data class AppProperties(var key1: String) {
    data class App(val key1: String? = null)
}