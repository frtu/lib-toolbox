package com.github.frtu.kotlin.protobuf

import com.github.frtu.proto.metadata.sec.Sec
import com.google.protobuf.Descriptors

class SecurityMetadataHelper : BaseFieldMetadataHelper<Boolean>(Sec.securedField) {
    fun isSecured(fieldDescriptor: Descriptors.FieldDescriptor?): Boolean {
        return if (hasExtension(fieldDescriptor)) {
            getExtension(fieldDescriptor)!!
        } else false
    }
}