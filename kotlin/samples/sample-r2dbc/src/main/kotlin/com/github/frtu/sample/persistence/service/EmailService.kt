package com.github.frtu.sample.persistence.service

import com.github.frtu.sample.persistence.r2dbc.json.EmailJson
import com.github.frtu.sample.persistence.r2dbc.json.entitytemplate.IEmailJsonRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class EmailService(val emailJsonRepository: IEmailJsonRepository) {
    suspend fun findById(id: UUID): EmailJson = emailJsonRepository.findById(id)

    suspend fun searchByQueryParams(searchParams: Map<String?, String?>, pageable: Pageable? = null): Flow<EmailJson> =
        emailJsonRepository.findAll(searchParams, pageable)
}

