package com.github.frtu.sample.rpc.grpc

import com.github.frtu.kotlin.protobuf.utils.toTimestamp
import com.github.frtu.sample.grpc.*
import com.github.frtu.sample.persistence.basic.IEmailRepository
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
        return emailHistoryItem {
            this.id = item.id.toString()
            this.creationTime = item.creationTime.toTimestamp()
            this.updateTime = item.updateTime.toTimestamp()
            this.status = item.status.toString()
            this.data = email {
                this.receiver = item.receiver!!
                this.subject = item.subject
                this.content = item.content
            }
        }
    }

    internal val logger = LoggerFactory.getLogger(this::class.java)
}