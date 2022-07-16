# Project - sample-grpc

## About

Sample gRPC using spring-boot & R2DBC :

* R2DBC Coroutine repository : ```com.github.frtu.sample.persistence```
* gRPC server-client implementation : ```com.github.frtu.sample.rpc.grpc```
* Adding all metrics

## Metrics

### gRPC

#### Throughput IN / OUT 

```
# HELP grpc_server_requests_received_messages_total The total number of requests received (counter)
grpc_server_requests_received_messages_total
# HELP grpc_server_responses_sent_messages_total The total number of responses sent (counter)
grpc_server_responses_sent_messages_total
```

#### Duration

```
# HELP grpc_server_processing_duration_seconds The total time taken for the server to complete the call (summary)
grpc_server_processing_duration_seconds_count
grpc_server_processing_duration_seconds_sum
# HELP grpc_server_processing_duration_seconds_max The total time taken for the server to complete the call (gauge)
grpc_server_processing_duration_seconds_max
```

### Database

#### Spring Data Repository

```
# HELP spring_data_repository_invocations_seconds_max (gauge)
spring_data_repository_invocations_seconds_max
# HELP spring_data_repository_invocations_seconds (summary)
spring_data_repository_invocations_seconds_count
spring_data_repository_invocations_seconds_sum
```

* exception="None",
* method="save",
* repository="IEmailRepository",
* state="SUCCESS" OR state="CANCELED"

#### R2DBC

```
# HELP r2dbc_pool_acquired_connections Size of successfully acquired connections which are in active use. (gauge)
r2dbc_pool_acquired_connections{name="connectionFactory",} 0.0
# HELP r2dbc_pool_max_allocated_connections Maximum size of allocated connections that this pool allows. (gauge)
r2dbc_pool_max_allocated_connections{name="connectionFactory",} 10.0
# HELP r2dbc_pool_idle_connections Size of idle connections in the pool. (gauge)
r2dbc_pool_idle_connections{name="connectionFactory",} 10.0
# HELP r2dbc_pool_allocated_connections Size of allocated connections in the pool which are in active use or idle. (gauge)
r2dbc_pool_allocated_connections{name="connectionFactory",} 10.0
# HELP r2dbc_pool_pending_connections Size of pending to acquire connections from the underlying connection factory. (gauge)
r2dbc_pool_pending_connections{name="connectionFactory",} 0.0
# HELP r2dbc_pool_max_pending_connections Maximum size of pending state to acquire connections that this pool allows. (gauge)
r2dbc_pool_max_pending_connections{name="connectionFactory",} 2.147483647E9
```

## See also

* [gRPC Kotlin startup](https://grpc.io/docs/languages/kotlin/)