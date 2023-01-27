package com.github.frtu.sample.sink

interface EmailSink {
    suspend fun emit(emailDetail: EmailDetail)
}