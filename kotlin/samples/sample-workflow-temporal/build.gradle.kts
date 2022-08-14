// https://github.com/grpc/grpc-kotlin/blob/master/compiler/README.md
import com.google.protobuf.gradle.*
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    java
    `java-library`
    kotlin("jvm")
    kotlin("plugin.spring") version Versions.kotlin
    id("org.springframework.boot") version Versions.spring_boot
    id("io.spring.dependency-management") version Versions.spring_boot_dep_mgmt
    id("com.google.protobuf") version Versions.plugin_protobuf
//    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
}
//apply(plugin = "org.jlleitschuh.gradle.ktlint")
apply(plugin = "idea")
apply(plugin = "org.jetbrains.kotlin.jvm")
apply(plugin = "org.jetbrains.kotlin.plugin.spring")
apply(plugin = "io.spring.dependency-management")

group = "com.github.frtu.sample.workflow.temporal"

dependencies {
    implementation("com.github.frtu.libs:starter-temporal:${Versions.frtu_libs}")

    implementation("io.temporal:temporal-sdk:${Versions.temporal}")
    implementation("io.temporal:temporal-kotlin:${Versions.temporal}")
    implementation("io.temporal:temporal-opentracing:${Versions.temporal}")

    implementation("io.serverlessworkflow:serverlessworkflow-api:${Versions.serverlessworkflow}")
    implementation("io.serverlessworkflow:serverlessworkflow-validation:${Versions.serverlessworkflow}")
    implementation("io.serverlessworkflow:serverlessworkflow-spi:${Versions.serverlessworkflow}")
    implementation("io.serverlessworkflow:serverlessworkflow-util:${Versions.serverlessworkflow}")
    implementation("net.thisptr:jackson-jq:${Versions.jq}")

    implementation("org.springframework.boot:spring-boot-starter")

    // Reactive database
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    runtimeOnly("io.r2dbc:r2dbc-h2")
    runtimeOnly("com.h2database:h2")
    implementation("io.r2dbc:r2dbc-postgresql")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.springframework.boot:spring-boot-starter-jdbc")
    runtimeOnly("org.flywaydb:flyway-core")

    // gRPC
    implementation("com.google.protobuf:protobuf-java:${Versions.protobuf}")
    implementation("com.google.protobuf:protobuf-java-util:${Versions.protobuf}")
    implementation("com.google.protobuf:protobuf-kotlin:${Versions.protobuf}")
    implementation("io.grpc:grpc-protobuf:${Versions.grpc}")
    implementation("io.grpc:grpc-kotlin-stub:${Versions.grpc_kotlin}")
    implementation("io.grpc:grpc-stub:${Versions.grpc}")
    implementation("io.grpc:grpc-netty:${Versions.grpc}")
    implementation("net.devh:grpc-server-spring-boot-starter:${Versions.grpc_spring_boot_starter}")

    // Kafka
    implementation(Libs.kafka)
    implementation(Libs.kafka_client)
    implementation(Libs.kafka_reactor)
    implementation("org.springframework.kafka:spring-kafka")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("${Libs.kafka_client}:test")

    // Serialization
    implementation(Libs.lib_serdes_protobuf)

    implementation(Libs.jackson_core)
    implementation(Libs.jackson_yaml)
    implementation(Libs.jackson_databind)
    implementation(Libs.jackson_module_kotlin)
    implementation(Libs.jackson_datatype_jsr310)

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springdoc:springdoc-openapi-webflux-ui:${Versions.springdoc}")
    // Dev & monitoring
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-core:${Versions.micrometer}")
//    implementation("io.micrometer:micrometer-registry-prometheus:${Versions.micrometer}")

    implementation("io.opentelemetry:opentelemetry-sdk")
    implementation("io.opentelemetry:opentelemetry-semconv")
    implementation("io.opentelemetry:opentelemetry-extension-trace-propagators")
    implementation("io.opentelemetry:opentelemetry-opentracing-shim")
    implementation("io.opentelemetry:opentelemetry-exporter-jaeger")
    implementation("io.jaegertracing:jaeger-client:${Versions.jaeger}")

    // Platform - Coroutine and Async
    implementation(Libs.coroutines_core)
    implementation(Libs.coroutines_core_jvm)
    implementation(Libs.coroutines_reactive)
    implementation(Libs.coroutines_reactor)

    // Platform - Log
    implementation(Libs.logger_core)
    implementation(Libs.log_impl)
    testImplementation(Libs.lib_utils)
    testImplementation(Libs.spring_core)

    // Test
    testImplementation(Libs.junit)
    testImplementation(Libs.kotest)
    testImplementation(Libs.kotest_json)
    testImplementation(Libs.kotest_property)
    testImplementation(Libs.awaitility)
    testImplementation(Libs.mock)
    testImplementation(Libs.assertions)
    testImplementation(kotlin("test"))
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Platform - BOMs
    testImplementation(platform(Libs.bom_kotest))
    implementation(platform(kotlin("bom")))
    implementation(kotlin("stdlib"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    if (JavaVersion.current().isJava9Compatible) {
        // Workaround for @javax.annotation.Generated
        // see: https://github.com/grpc/grpc-java/issues/3633
        implementation("javax.annotation:javax.annotation-api:1.3.2")
    }
}

the<DependencyManagementExtension>().apply {
    imports {
        mavenBom(SpringBootPlugin.BOM_COORDINATES)
        mavenBom(Libs.bom_springboot)
        mavenBom(Libs.bom_jackson)
        mavenBom(Libs.bom_kotlin_base)
        mavenBom(Libs.bom_kotlin_libs)
        mavenBom(Libs.bom_logger)
        mavenBom(Libs.bom_opentelemetry)
        mavenBom(Libs.bom_opentelemetry_alpha)
    }
}

sourceSets {
    create("proto") {
        proto {
            srcDir("src/main/proto")
        }
    }
}
protobuf {
    protoc { artifact = "com.google.protobuf:protoc:${Versions.protobuf}" }
    plugins {
        id("grpc") { artifact = "io.grpc:protoc-gen-grpc-java:${Versions.grpc}" }
        id("grpckt") { artifact = "io.grpc:protoc-gen-grpc-kotlin:${Versions.plugin_grpc_kotlin}" }
    }
    // generatedFilesBaseDir = "$projectDir/generated"
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
            it.builtins {
                id("kotlin")
            }
        }
    }
}

java {
    sourceCompatibility = JavaVersion.toVersion(Versions.java)
    targetCompatibility = JavaVersion.toVersion(Versions.java)
    withSourcesJar()
}
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = Versions.java
        languageVersion = Versions.language
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xopt-in=kotlin.RequiresOptIn")
    }
}
repositories {
    mavenLocal()
    mavenCentral()
}
