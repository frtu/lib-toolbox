package com.github.frtu.sample.broker.service

import com.github.frtu.sample.persistence.r2dbc.basic.Email
import com.github.frtu.sample.persistence.r2dbc.basic.IEmailRepository
import org.springframework.stereotype.Service

@Service
class MessageConsumerService constructor(private val repository: IEmailRepository) {
    suspend fun processProductMessage(message: Email) {
        repository.save(message)
    }
}
