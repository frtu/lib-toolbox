package com.github.frtu.sample.rpc.grpc

import com.github.frtu.logs.core.RpcLogger
import com.github.frtu.sample.domain.EmailCrudHandler
import com.github.frtu.sample.grpc.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.devh.boot.grpc.server.service.GrpcService
import org.springframework.stereotype.Component

@Component
@GrpcService
class EmailRepositoryServiceImpl(
    private val emailCrudHandler: EmailCrudHandler,
) : EmailRepositoryServiceGrpcKt.EmailRepositoryServiceCoroutineImplBase() {

    override suspend fun queryOne(request: ById): EmailHistoryItem =
        emailCrudHandler.queryOne(request.id)
            ?.let { it.toEmailHistoryItem() }
            ?: throw IllegalArgumentException("${request.id} doesn't exist")

    override fun queryMany(request: By): Flow<EmailHistoryItem> =
        emailCrudHandler.queryMany().map { it.toEmailHistoryItem() }

    override fun insert(requests: Flow<EmailHistoryItem>): Flow<Id> =
        emailCrudHandler.insertMany(
            requests.map { it.toEmailEntity() }
        ).map {
            rpcLogger.debug("ID=$it")
            Id.newBuilder().setId(it).build()
        }

    internal val rpcLogger = RpcLogger.create(this::class.java)
}
