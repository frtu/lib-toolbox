package com.github.frtu.kotlin.flow.core

interface Flow<INPUT, OUTPUT> {
    val flowName: String
    fun execute(input: INPUT): OUTPUT
}