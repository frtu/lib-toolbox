object Versions {
    const val java = "11"

    // Allow to enable >1.4 syntax
    const val language = "1.7"
    const val kotlin = "1.7.20"
    const val kotlinx = "1.6.3"

    const val jackson = "2.13.2.1" // 2022-03-30
    const val spring_boot = "2.7.10" // 2023-03-23
    const val spring_boot_dep_mgmt = "1.0.15.RELEASE" // 2023-03-23
    const val micrometer = "1.9.0"
    const val springdoc = "1.5.4"
    const val frtu_base = "1.2.4"
    const val frtu_libs = "1.2.5"
    const val frtu_logs = "1.1.5"
    const val awaitility = "4.2.0" // 2022-03-04
    const val kotest = "5.4.2" // 2022-08-10
    const val opentelemetry = "1.15.0" // 2022-06-25
    const val jaeger = "1.8.0"
    const val prometheus = "0.16.0"

    const val plugin_jacoco = "0.8.8" // 2022-04-05
    const val plugin_google_format = "0.9" // 2020-06-09
    const val plugin_protobuf = "0.8.18"
    const val plugin_grpc_kotlin = "0.1.5"

    const val kafka = "2.6.0"
    const val kafka_reactor = "1.3.11"

    const val protobuf = "3.21.1" // 2022-05-28
    const val grpc = "1.46.0" // 2022-04-26
    const val grpc_kotlin = "1.3.0" // 2022-05-28
    const val grpc_spring_boot_starter = "2.13.1.RELEASE"

    const val temporal = "1.17.0"
    const val serverlessworkflow = "4.0.3.Final"
    const val jq = "1.0.0-preview.20220705"
}

object Libs {
    const val kafka = "org.apache.kafka:kafka_2.13:${Versions.kafka}"
    const val kafka_client = "org.apache.kafka:kafka-clients:${Versions.kafka}"
    const val kafka_reactor = "io.projectreactor.kafka:reactor-kafka:${Versions.kafka_reactor}"

    //---------- Commons -----------
    const val jackson_core = "com.fasterxml.jackson.core:jackson-core"
    const val jackson_databind = "com.fasterxml.jackson.core:jackson-databind"
    const val jackson_module_kotlin = "com.fasterxml.jackson.module:jackson-module-kotlin"
    const val jackson_datatype_jsr310 = "com.fasterxml.jackson.datatype:jackson-datatype-jsr310"
    const val jackson_yaml = "com.fasterxml.jackson.dataformat:jackson-dataformat-yaml"
    const val lib_serdes_protobuf = "com.github.frtu.libs:lib-serdes-protobuf:${Versions.frtu_libs}"
    const val lib_utils = "com.github.frtu.libs:lib-utils"
    const val spring_core = "org.springframework:spring-core"

    //---------- COROUTINE -----------
    const val coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinx}"
    const val coroutines_core_jvm = "org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:${Versions.kotlinx}"
    const val coroutines_reactive = "org.jetbrains.kotlinx:kotlinx-coroutines-reactive:${Versions.kotlinx}"
    const val coroutines_reactor = "org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${Versions.kotlinx}"

    //---------- LOGS -----------
    // Implementation for slf4j
    const val log_impl = "ch.qos.logback:logback-classic"
    const val logger_core = "com.github.frtu.logs:logger-core"

    //---------- LIBS BOM -----------
    const val bom_springboot = "org.springframework.boot:spring-boot-dependencies:${Versions.spring_boot}"
    const val bom_jackson = "com.fasterxml.jackson:jackson-bom:${Versions.jackson}"
    const val bom_kotlin_base = "com.github.frtu.archetype:kotlin-base-pom:${Versions.frtu_base}"
    const val bom_kotlin_libs = "com.github.frtu.libs:lib-kotlin-bom:${Versions.frtu_libs}"
    const val bom_logger = "com.github.frtu.logs:logger-bom:${Versions.frtu_logs}"
    const val bom_opentelemetry = "io.opentelemetry:opentelemetry-bom:${Versions.opentelemetry}"
    const val bom_opentelemetry_alpha = "io.opentelemetry:opentelemetry-bom-alpha:${Versions.opentelemetry}-alpha"

    //---------- TESTS -----------
    const val bom_kotest = "io.kotest:kotest-bom:${Versions.kotest}"

    const val kotest = "io.kotest:kotest-assertions-core"
    const val kotest_json = "io.kotest:kotest-assertions-json"
    const val kotest_property = "io.kotest:kotest-property"

    const val junit = "org.junit.jupiter:junit-jupiter"
    const val awaitility = "org.awaitility:awaitility-kotlin:${Versions.awaitility}"

    // Mock lib mockk or mockito
    const val mock = "io.mockk:mockk"
    const val assertions = "org.assertj:assertj-core"
}
