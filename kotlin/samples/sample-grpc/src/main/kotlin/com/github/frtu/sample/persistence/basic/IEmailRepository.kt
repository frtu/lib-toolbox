package com.github.frtu.sample.persistence.basic

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface IEmailRepository : CoroutineCrudRepository<EmailEntity, UUID> {
}
