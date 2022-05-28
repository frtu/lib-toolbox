package com.github.frtu.kotlin.protobuf

import com.github.frtu.kotlin.protobuf.utils.MetadataUtil
import com.github.frtu.proto.metadata.Person
import com.google.protobuf.Descriptors
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

internal class SecurityMetadataHelperTest {
    private val securityMetadataHelper: SecurityMetadataHelper = SecurityMetadataHelper()

    @Test
    fun isSecured() {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        val descriptor: Descriptors.Descriptor = Person.getDescriptor()
        val fieldDescriptorMap: Map<String, Descriptors.FieldDescriptor> = MetadataUtil.buildFieldsMap(descriptor)
        logger.debug(fieldDescriptorMap.toString())

        //--------------------------------------
        // 2 & 3. Execute & Validate
        //--------------------------------------
        assertThat(securityMetadataHelper.isSecured(fieldDescriptorMap["name"])).isFalse
        assertThat(securityMetadataHelper.isSecured(fieldDescriptorMap["id"])).isFalse
        assertThat(securityMetadataHelper.isSecured(fieldDescriptorMap["email"])).isTrue
        assertThat(securityMetadataHelper.isSecured(fieldDescriptorMap["phones"])).isFalse
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}