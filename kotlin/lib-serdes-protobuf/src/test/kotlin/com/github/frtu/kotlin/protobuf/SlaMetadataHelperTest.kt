package com.github.frtu.kotlin.protobuf

import com.github.frtu.proto.metadata.Person
import com.github.frtu.proto.metadata.sla.DataSLA
import com.google.protobuf.Descriptors
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

internal class SlaMetadataHelperTest {
    private val slaMetadataHelper = SlaMetadataHelper()

    @Test
    fun getSLA() {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        val personDescriptor: Descriptors.Descriptor = Person.getDescriptor()

        //--------------------------------------
        // 2 & 3. Execute & Validate
        //--------------------------------------
        assertThat(slaMetadataHelper.hasSLA(personDescriptor)).isTrue
        val dataSLA: DataSLA = slaMetadataHelper.getSLA(personDescriptor)!!
        logger.debug(dataSLA.toString())
        assertThat(dataSLA).isEqualTo(DataSLA.RELIABLE)
    }

    private val logger = LoggerFactory.getLogger(this::class.java)
}