package com.github.frtu.sample.rpc.grpc

import com.github.frtu.logs.core.RpcLogger
import com.github.frtu.sample.domain.EmailHandler
import com.github.frtu.sample.grpc.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.devh.boot.grpc.server.service.GrpcService
import org.springframework.stereotype.Component

@Component
@GrpcService
class EmailRepositoryServiceImpl(
    private val emailHandler: EmailHandler,
) : EmailRepositoryServiceGrpcKt.EmailRepositoryServiceCoroutineImplBase() {

    override suspend fun queryOne(request: ById): EmailHistoryItem =
        emailHandler.queryOne(request.id)
            ?.let { it.toEmailHistoryItem() }
            ?: throw IllegalArgumentException("${request.id} doesn't exist")

    override fun queryMany(request: By): Flow<EmailHistoryItem> =
        emailHandler.queryMany().map { it.toEmailHistoryItem() }

    override fun insert(requests: Flow<EmailHistoryItem>): Flow<Id> =
        emailHandler.insertMany(
            requests.map { it.toEmailEntity() }
        ).map {
            rpcLogger.debug("ID=$it")
            Id.newBuilder().setId(it).build()
        }

    internal val rpcLogger = RpcLogger.create(this::class.java)
}