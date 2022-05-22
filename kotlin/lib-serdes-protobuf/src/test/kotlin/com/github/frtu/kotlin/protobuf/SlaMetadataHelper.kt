package com.github.frtu.kotlin.protobuf

import com.github.frtu.proto.metadata.sla.DataSLA
import com.github.frtu.proto.metadata.sla.Sla
import com.google.protobuf.Descriptors
import java.util.*

class SlaMetadataHelper : BaseMessageMetadataHelper<DataSLA>(Sla.dataSla) {
    fun hasSLA(messageDescriptor: Descriptors.Descriptor?): Boolean {
        return super.hasExtension(messageDescriptor)
    }

    fun getSLA(messageDescriptor: Descriptors.Descriptor?): Optional<DataSLA> {
        return super.getExtension(messageDescriptor)
    }
}