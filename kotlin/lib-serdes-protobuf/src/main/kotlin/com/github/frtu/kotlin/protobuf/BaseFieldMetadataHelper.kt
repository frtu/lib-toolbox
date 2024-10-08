package com.github.frtu.kotlin.protobuf

import com.google.protobuf.DescriptorProtos.FieldOptions
import com.google.protobuf.Descriptors
import com.google.protobuf.GeneratedMessage
import java.util.*

open class BaseFieldMetadataHelper<T>(
    private val fieldExtention: GeneratedMessage.GeneratedExtension<FieldOptions, T>,
) {
    fun getExtension(fieldDescriptor: Descriptors.FieldDescriptor?): T? {
        if (fieldDescriptor == null) {
            return null
        }
        val options = fieldDescriptor.options
        return options.getExtension(fieldExtention)
    }

    fun hasExtension(fieldDescriptor: Descriptors.FieldDescriptor?): Boolean {
        return getExtension(fieldDescriptor) != null
    }
}
