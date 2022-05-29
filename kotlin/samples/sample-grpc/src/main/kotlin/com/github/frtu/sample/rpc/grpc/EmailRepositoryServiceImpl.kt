package com.github.frtu.sample.rpc.grpc

import com.github.frtu.sample.grpc.*
import com.github.frtu.sample.persistence.basic.IEmailRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.devh.boot.grpc.server.service.GrpcService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

@Component
@GrpcService
class EmailRepositoryServiceImpl(
    private val coroutineRepository: IEmailRepository,
) : EmailRepositoryServiceGrpcKt.EmailRepositoryServiceCoroutineImplBase() {

    override suspend fun queryOne(request: ById): EmailHistoryItem {
        logger.info("queryOne(ById[${request.id}])")
        val item = coroutineRepository.findById(UUID.fromString(request.id))!!
        return item.toEmailHistoryItem()
    }

    override fun queryMany(request: By): Flow<EmailHistoryItem> =
        coroutineRepository.findAll().map { it.toEmailHistoryItem() }

    override fun insert(requests: Flow<EmailHistoryItem>): Flow<Id> =
        coroutineRepository
            .saveAll(requests.map { it.toEmailEntity() })
            .map {
                println("Saving uuid ${it.id}")
                id { it.id }
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