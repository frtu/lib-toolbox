import org.gradle.api.tasks.diagnostics.internal.dependencies.AsciiDependencyReportRenderer
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.google.protobuf.gradle.*

object Versions {
    // Workflow
    const val TEMPORAL_VERSION = "1.18.1"
    const val SERVERLESS_WORKFLOW_VERSION = "4.0.3.Final"
    const val JQ = "1.0.0-preview.20220705"

    // Commons
    const val JACKSON_VERSION = "2.13.4"

    // frtu
    const val FRTU_LIBS = "1.2.5"
    const val FRTU_LOGS = "1.1.5"

    // gRPC
    const val PROTOBUF = "3.21.1" // 2022-05-28
    const val GRPC = "1.46.0" // 2022-04-26
    const val GRPC_KOTLIN = "1.3.0" // 2022-05-28
    const val GRPC_SPRING_BOOT_STARTER = "2.13.1.RELEASE"
    const val PLUGIN_GRPC_KOTLIN = "0.1.5"

    // Spring platform
    const val SPRING_BOOT_STARTER = "2.7.10"
    const val SPRING_DOC = "1.6.9" // 2022-05-27
    const val R2DBC = "Arabba-SR13" // 2022-03-18

    val JVM_TARGET = JavaVersion.VERSION_11.toString()

    // Allow to enable >1.4 syntax
    const val LANGUAGE_VERSION = "1.7"
    const val KOTLIN = "1.7.20"
    const val KOTLINX = "1.6.3"
    const val JUNIT = "5.8.2"
    const val LOG4J = "2.19.0" // 2022-09-14
    const val OPENTELEMETRY = "1.23.1" // 2023-02-16
    const val MOCKK = "1.12.7"
    const val KOTEST = "5.4.2" // 2022-08-10
    const val AWAITILITY = "4.2.0" // 2022-03-04
    const val TESTCONTAINERS = "1.16.0"

    const val PLUGIN_JACOCO = "0.8.8" // 2022-04-05
}

plugins {
    val kotlin = "1.7.20"
    val springBoot = "2.7.10"
    val springDeptMgmt = "1.0.15.RELEASE"
    val pluginProtobuf = "0.8.18"
    val ktlint = "11.0.0"

    // Core
    application
    kotlin("jvm")
    // gRPC
    id("com.google.protobuf") version pluginProtobuf
    // Spring
    kotlin("plugin.spring") version kotlin
    id("org.springframework.boot") version springBoot
    id("io.spring.dependency-management") version springDeptMgmt
    // SDLC
//    id("org.jlleitschuh.gradle.ktlint") version ktlint
    jacoco
    pmd
    idea
}

group = "com.github.frtu.sample"

apply {
    plugin("io.spring.dependency-management")
//    plugin("org.jlleitschuh.gradle.ktlint")
    plugin("project-report")
    plugin("jacoco")
    plugin("idea")
}

configure<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension> {
    dependencies {
        with(Versions) {
            // Temporal
            dependencySet("io.temporal:$TEMPORAL_VERSION") {
                entry("temporal-sdk")
                entry("temporal-kotlin")
                entry("temporal-opentracing")
                entry("temporal-serviceclient")
                entry("temporal-remote-data-encoder")
                entry("temporal-testing")
            }
            // Serverless workflow
            dependencySet("io.serverlessworkflow:$SERVERLESS_WORKFLOW_VERSION") {
                entry("serverlessworkflow-api")
                entry("serverlessworkflow-validation")
                entry("serverlessworkflow-spi")
                entry("serverlessworkflow-util")
                entry("serverlessworkflow-diagram")
            }
            dependency("net.thisptr:jackson-jq:$JQ")

            dependency("com.google.protobuf:protobuf-kotlin:$PROTOBUF")
            dependency("io.grpc:grpc-kotlin-stub:$GRPC_KOTLIN")
            dependency("net.devh:grpc-server-spring-boot-starter:$GRPC_SPRING_BOOT_STARTER")

            // Spring platform
            dependency("org.springdoc:springdoc-openapi-webflux-ui:$SPRING_DOC")

            // Common platform
            dependency("io.mockk:mockk:$MOCKK")
            dependency("org.awaitility:awaitility-kotlin:$AWAITILITY")

            dependency("com.github.frtu.libs:starter-temporal:$FRTU_LIBS")
            imports {
                mavenBom("com.github.frtu.libs:lib-kotlin-bom:$FRTU_LIBS")
                mavenBom("com.github.frtu.logs:logger-bom:$FRTU_LOGS")

                mavenBom("com.google.protobuf:protobuf-bom:$PROTOBUF")
                mavenBom("io.grpc:grpc-bom:$GRPC")

                mavenBom("io.r2dbc:r2dbc-bom:$R2DBC")

                mavenBom("com.fasterxml.jackson:jackson-bom:$JACKSON_VERSION")
                mavenBom("org.apache.logging.log4j:log4j-bom:$LOG4J")
                mavenBom("org.jetbrains.kotlin:kotlin-bom:$KOTLIN")
                mavenBom("org.jetbrains.kotlinx:kotlinx-coroutines-bom:$KOTLINX")
                mavenBom("io.opentelemetry:opentelemetry-bom:$OPENTELEMETRY")
                mavenBom("io.opentelemetry:opentelemetry-bom-alpha:$OPENTELEMETRY-alpha")
                mavenBom("io.kotest:kotest-bom:$KOTEST")
                mavenBom("org.testcontainers:testcontainers-bom:$TESTCONTAINERS")
            }
        }
    }
}

dependencies {
    // Workfkow
    implementation("com.github.frtu.libs:starter-temporal")

    implementation("io.temporal:temporal-sdk")
    implementation("io.temporal:temporal-kotlin")
    implementation("io.temporal:temporal-opentracing")

    implementation("io.serverlessworkflow:serverlessworkflow-api")
    implementation("io.serverlessworkflow:serverlessworkflow-validation")
    implementation("io.serverlessworkflow:serverlessworkflow-spi")
    implementation("io.serverlessworkflow:serverlessworkflow-util")
    implementation("net.thisptr:jackson-jq")

    implementation("org.apache.kafka:kafka_2.13")
    implementation("org.apache.kafka:kafka-clients")
    implementation("io.projectreactor.kafka:reactor-kafka")
    implementation("org.springframework.kafka:spring-kafka")
    testImplementation("org.springframework.kafka:spring-kafka-test")

    // Spring Reactive
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springdoc:springdoc-openapi-webflux-ui")

    // Storage
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("io.r2dbc:r2dbc-h2")
    runtimeOnly("com.h2database:h2")
//    implementation("io.r2dbc:r2dbc-postgresql")
//    runtimeOnly("org.postgresql:postgresql")

    runtimeOnly("org.springframework.boot:spring-boot-starter-jdbc")
    runtimeOnly("org.flywaydb:flyway-core")

    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // gRPC
    implementation("com.google.protobuf:protobuf-java")
    implementation("com.google.protobuf:protobuf-java-util")
    implementation("com.google.protobuf:protobuf-kotlin")
    implementation("io.grpc:grpc-protobuf")
    implementation("io.grpc:grpc-kotlin-stub")
    implementation("io.grpc:grpc-stub")
    implementation("io.grpc:grpc-netty")
    implementation("net.devh:grpc-server-spring-boot-starter")

    // DevTools and Monitoring
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    // Serialization
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // Platform - Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

    // Spring dev and monitoring
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-core")
    implementation("io.micrometer:micrometer-registry-prometheus")

    // Platform - Observability
    implementation("io.opentelemetry:opentelemetry-sdk")
    implementation("io.opentelemetry:opentelemetry-extension-annotations")
    implementation("io.opentelemetry:opentelemetry-extension-trace-propagators")
    implementation("io.opentelemetry:opentelemetry-semconv")

    // Platform - Log
    implementation("com.github.frtu.logs:logger-core")
    implementation("ch.qos.logback:logback-classic")

    // Platform test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")

    // Test
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.kotest:kotest-runner-junit5")
    testImplementation("io.kotest:kotest-assertions-core")
    testImplementation("io.kotest:kotest-assertions-json")
    testImplementation("io.kotest:kotest-property")
    testImplementation("org.awaitility:awaitility-kotlin")
    testImplementation("io.mockk:mockk")
    testImplementation("com.github.frtu.libs:lib-utils")

    // Platform - BOMs
    implementation(platform(kotlin("bom")))
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    if (JavaVersion.current().isJava9Compatible) {
        // Workaround for @javax.annotation.Generated
        // see: https://github.com/grpc/grpc-java/issues/3633
        implementation("javax.annotation:javax.annotation-api:1.3.2")
    }
}

configurations.all {
    exclude(group = "log4j", module = "log4j")
    exclude(group = "junit", module = "junit")
    exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    exclude(group = "io.projectreactor.netty", module = "reactor-netty-http-brave")
    exclude(group = "org.jetbrains.kotlin", module = "kotlin-coroutines-experimental-compat")

    with(Versions) {
        resolutionStrategy.eachDependency {
            if (requested.group == "org.jetbrains.kotlin") {
                useVersion(KOTLIN)
            }
            if (requested.group == "org.junit") {
                useVersion(JUNIT)
            }
            if (requested.group == "org.springframework.boot") {
                useVersion(SPRING_BOOT_STARTER)
            }
        }
    }
}

task("allDependencies", DependencyReportTask::class) {
    evaluationDependsOnChildren()
    this.setRenderer(AsciiDependencyReportRenderer())
}
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = Versions.JVM_TARGET
        languageVersion = Versions.LANGUAGE_VERSION
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}
java {
    sourceCompatibility = JavaVersion.toVersion(Versions.JVM_TARGET)
    targetCompatibility = JavaVersion.toVersion(Versions.JVM_TARGET)
    withSourcesJar()
}

jacoco {
    toolVersion = Versions.PLUGIN_JACOCO
}
tasks {
    test {
        useJUnitPlatform()
    }
    jacocoTestReport {
        reports {
            xml.required.set(true)
            html.required.set(true)
            csv.required.set(false)
        }
        dependsOn(test)
    }
    jacocoTestCoverageVerification {
        violationRules {
            // Configure the ratio based on your standard
            rule { limit { minimum = BigDecimal.valueOf(0.0) } }
        }
    }
    check {
        dependsOn(jacocoTestCoverageVerification)
    }
    idea {
        module {
            isDownloadJavadoc = true
            isDownloadSources = true
        }
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
    with(Versions) {
        if (osdetector.os == "osx") {
            protoc { artifact = "com.google.protobuf:protoc:$PROTOBUF:osx-x86_64" }
            plugins {
                id("grpc") { artifact = "io.grpc:protoc-gen-grpc-java:$GRPC:osx-x86_64" }
                id("grpckt") { artifact = "io.grpc:protoc-gen-grpc-kotlin:$PLUGIN_GRPC_KOTLIN:osx-x86_64" }
            }
        } else {
            protoc { artifact = "com.google.protobuf:protoc:$PROTOBUF" }
            plugins {
                id("grpc") { artifact = "io.grpc:protoc-gen-grpc-java:$GRPC" }
                id("grpckt") { artifact = "io.grpc:protoc-gen-grpc-kotlin:$PLUGIN_GRPC_KOTLIN" }
            }
        }
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

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.spring.io/milestone")
}

application {
    // Define the main class for the application.
    mainClass.set("com.github.frtu.sample.ApplicationKt")
}
