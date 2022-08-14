package com.github.frtu.workflow.temporal.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

/**
 * Temporal stub configuration properties starting with 'temporal.stub'
 *
 * @author Frédéric TU
 * @since 1.2.2
 */
@ConfigurationProperties(prefix = "temporal.stub")
class TemporalStubProperties(
    var identity: String? = null,
    var namespace: String = "default",
    var binaryChecksum: String? = null,

    var target: String = "localhost:7233",
    var enableHttps: Boolean = false,

    var enableKeepAlive: Boolean = false,
    var keepAliveTime: Duration? = null,
    var keepAliveTimeout: Duration? = null,
    var keepAlivePermitWithoutStream: Boolean = false,
    var connectionBackoffResetFrequency: Duration? = null,
    var grpcReconnectFrequency: Duration? = null,

    var rpcQueryTimeout: Duration? = null,
    var rpcTimeout: Duration? = null,
    var rpcLongPollTimeout: Duration? = null,
)