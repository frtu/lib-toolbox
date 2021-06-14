package com.github.frtu.sample.persistence.service

import com.github.frtu.sample.persistence.r2dbc.Email
import com.github.frtu.sample.persistence.r2dbc.entitytemplate.IEmailRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class EmailService(val emailRepository: IEmailRepository) {
    suspend fun findById(id: Long): Email = emailRepository.findById(id)

    suspend fun searchByQueryParams(searchParams: Map<String?, String?>, pageable: Pageable? = null): Flow<Email> =
        emailRepository.findAll(searchParams, pageable)
}

