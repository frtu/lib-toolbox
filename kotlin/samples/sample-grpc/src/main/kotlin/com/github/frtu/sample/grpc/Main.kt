package com.github.frtu.sample.grpc

import com.github.frtu.sample.grpc.metadata.sla.DataSLA
import com.google.protobuf.Descriptors

private val slaMetadataHelper = SlaMetadataHelper()

fun main(args: Array<String>) {
    val personDescriptor: Descriptors.Descriptor = Email.getDescriptor()
    val dataSLA: DataSLA = slaMetadataHelper.getSLA(personDescriptor).get()
    System.out.println(dataSLA.toString())
}
