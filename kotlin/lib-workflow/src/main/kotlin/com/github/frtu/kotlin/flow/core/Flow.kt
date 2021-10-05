package com.github.frtu.kotlin.flow.core

interface Flow<INPUT, OUTPUT> {
    val name: String
    fun execute(input: INPUT): OUTPUT
}