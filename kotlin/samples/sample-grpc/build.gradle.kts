// https://github.com/grpc/grpc-kotlin/blob/master/compiler/README.md
import com.google.protobuf.gradle.*
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    kotlin("jvm")
    kotlin("plugin.spring") version Versions.kotlin
    id("org.springframework.boot") version Versions.spring_boot
    id("com.google.protobuf") version Versions.plugin_protobuf
//    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
}
//apply(plugin = "org.jlleitschuh.gradle.ktlint")
apply(plugin = "idea")
apply(plugin = "org.jetbrains.kotlin.jvm")
apply(plugin = "org.jetbrains.kotlin.plugin.spring")
apply(plugin = "io.spring.dependency-management")

group = "com.github.frtu.sample.grpc"

dependencies {
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
    implementation("com.google.protobuf:protobuf-java")
    implementation("com.google.protobuf:protobuf-java-util")
    implementation("com.google.protobuf:protobuf-kotlin:${Versions.protobuf}")
    implementation("io.grpc:grpc-protobuf")
    implementation("io.grpc:grpc-stub")
    implementation("io.grpc:grpc-netty")
    implementation("io.grpc:grpc-kotlin-stub:${Versions.grpc_kotlin}")
    implementation("net.devh:grpc-server-spring-boot-starter:${Versions.grpc_spring_boot_starter}")

    // Serialization
    implementation(Libs.lib_serdes_protobuf)
    implementation(Libs.jackson_databind)
    implementation(Libs.jackson_module_kotlin)

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springdoc:springdoc-openapi-webflux-ui:${Versions.springdoc}")
    // Dev & monitoring
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-core:1.6.2")

    // Platform - Coroutine
    implementation(Libs.coroutines_reactor)

    // Platform - Log
    implementation(Libs.logger_core)
    implementation(Libs.log_impl)
    testImplementation(Libs.lib_utils)
    testImplementation(Libs.spring_core)

    // Test
    testImplementation(Libs.junit)
    testImplementation(Libs.mock)
    testImplementation(Libs.assertions)
    testImplementation(kotlin("test"))
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

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

the<DependencyManagementExtension>().apply {
    imports {
        mavenBom(SpringBootPlugin.BOM_COORDINATES)
        mavenBom(Libs.bom_jackson)
        mavenBom(Libs.bom_kotlin_base)
        mavenBom(Libs.bom_kotlin_libs)
        mavenBom(Libs.bom_logger)
        mavenBom(Libs.bom_protobuf)
        mavenBom(Libs.bom_grpc)
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
