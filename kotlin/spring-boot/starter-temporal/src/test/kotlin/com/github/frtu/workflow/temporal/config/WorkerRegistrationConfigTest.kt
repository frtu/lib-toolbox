package com.github.frtu.workflow.temporal.config

import com.github.frtu.sample.temporal.activity.EmailSinkActivityImpl
import com.github.frtu.sample.temporal.activity.TASK_QUEUE_EMAIL
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.temporal.worker.Worker
import io.temporal.worker.WorkerFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@DisplayName("Testing bootstrap with WorkerRegistrationConfig")
@ExtendWith(MockKExtension::class)
internal class WorkerRegistrationConfigTest {
    @MockK
    lateinit var worker: Worker

    @BeforeEach
    fun beforeEach() {
        clearMocks(worker)
    }

    @Test
    fun `handleContextRefresh finding bean annotated bean`() {
        val applicationContext = AnnotationConfigApplicationContext(ActivityConfigWithBean::class.java)

        //--------------------------------------
        // 1. Prepare
        //--------------------------------------
        val taskQueueSlot = slot<String>()
        val workerFactoryMock = applicationContext.getBean(WorkerFactory::class.java)
        every { workerFactoryMock.newWorker(capture(taskQueueSlot)) } returns worker

        val activityImplementationCaptured = mutableListOf<Any?>()
        every { worker.registerActivitiesImplementations(*varargAllNullable { activityImplementationCaptured.add(it) }) } returns Unit

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val workerRegistrationConfig = WorkerRegistrationConfig(applicationContext)
        workerRegistrationConfig.handleContextRefresh()

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        // Test registration has been called
        verify(exactly = 1) { workerFactoryMock.newWorker(any()) }
        verify(exactly = 1) { worker.registerActivitiesImplementations(any()) }

        // Test values are as expected
        assertThat(taskQueueSlot.captured).isEqualTo(TASK_QUEUE_EMAIL)
        assertThat(activityImplementationCaptured.size).isEqualTo(1)
        assertThat(activityImplementationCaptured[0]?.javaClass).isEqualTo(EmailSinkActivityImpl::class.java)
    }

    @Test
    fun `handleContextRefresh without matches`() {
        val applicationContext = AnnotationConfigApplicationContext(ActivityConfigWithoutBean::class.java)
        //--------------------------------------
        // 1. Prepare
        //--------------------------------------
        val workerFactoryMock = applicationContext.getBean(WorkerFactory::class.java)

        //--------------------------------------
        // 2. Execute
        //--------------------------------------
        val workerRegistrationConfig = WorkerRegistrationConfig(applicationContext)
        workerRegistrationConfig.handleContextRefresh()

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
//        verify(exactly = 0) { workerFactoryMock.newWorker(any()) }
//        verify(exactly = 0) { worker.registerActivitiesImplementations(any()) }
    }
}

@Configuration
class ActivityConfigWithoutBean {
    @Bean
    fun workerFactory(): WorkerFactory = mockk(relaxed = true)
}

@Configuration
@ComponentScan("com.github.frtu.sample.temporal")
class ActivityConfigWithBean : ActivityConfigWithoutBean()