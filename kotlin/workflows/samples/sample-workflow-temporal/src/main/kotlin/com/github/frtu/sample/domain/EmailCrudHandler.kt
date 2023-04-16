package com.github.frtu.sample.domain

import com.github.frtu.sample.persistence.basic.EmailEntity
import com.github.frtu.sample.persistence.basic.IEmailRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class EmailCrudHandler(
    private val coroutineRepository: IEmailRepository,
) {
    suspend fun queryOne(id: String): EmailEntity? {
        logger.info("queryOne(ById[$id])")
        return coroutineRepository.findById(id)
    }

    fun queryMany(): Flow<EmailEntity> =
        coroutineRepository.findAll()

    suspend fun insertOne(emailEntity: EmailEntity): String? =
        coroutineRepository.save(emailEntity).id

    fun insertMany(requests: Flow<EmailEntity>): Flow<String> =
        coroutineRepository
            .saveAll(requests)
            .map {
                println("Saving uuid ${it.id}")
                it.id!!
            }
//    TESTING BACK PRESSURE
//        flow {
//            while (true) {
//                val uuid = UUID.randomUUID()
//                println("Saving uuid $uuid")
//                emit(id { uuid })
//            }
//        }

    internal val logger = LoggerFactory.getLogger(this::class.java)
}
