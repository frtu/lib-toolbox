package com.github.frtu.workflow.temporal.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "temporal.stub")
class TemporalStubProperties(
    val target: String = "localhost:7233",
    val enableHttps: Boolean = false,

    val enableKeepAlive: Boolean = false,
    val keepAliveTime: Duration? = null,
    val keepAliveTimeout: Duration? = null,
    val keepAlivePermitWithoutStream: Boolean = false,
    val connectionBackoffResetFrequency: Duration? = null,
    val grpcReconnectFrequency: Duration? = null,

    val rpcQueryTimeout: Duration? = null,
    val rpcTimeout: Duration? = null,
    val rpcLongPollTimeout: Duration? = null,
)