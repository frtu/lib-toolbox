package com.github.frtu.sample.temporal.activity

import com.github.frtu.sample.sink.EmailDetail
import com.github.frtu.sample.sink.EmailSink
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class EmailSinkActivityImpl(
    private val emailSink: EmailSink,
) : EmailSinkActivity {
    override fun emit(emailDetail: EmailDetail) = runBlocking {
        emailSink.emit(emailDetail)
    }
}