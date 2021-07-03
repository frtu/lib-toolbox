package com.github.frtu.kotlin.utils

import org.apache.avro.Schema
import org.apache.avro.io.DatumReader
import org.apache.avro.io.Decoder
import org.apache.avro.io.DecoderFactory
import org.apache.avro.specific.SpecificDatumReader

/**
 * Allow to easily deserialize an Avro object from external source
 *
 * @author Frédéric TU
 * @since 1.1.2
 */
class AvroBeanHelper<T>(
    private val schema: Schema? = null,
    private val clazz: Class<T>? = null
) : BeanHelper() {
    /**
     * ONLY when full parameters has been passed during constructor call.
     */
    fun jsonFileToBean(location: String): T? = jsonFileToBean(this.schema!!, this.clazz!!, location)

    fun <T> jsonFileToBean(schema: Schema, clazz: Class<T>, location: String): T? {
        logger.debug("Deserialize class:${clazz} from location:${location}")
        val text = readFromFile(location)
        text?.let {
            return toBean(schema, clazz, text)
        }
        return null
    }

    fun toBean(text: String): T? {
        return toBean(schema!!, clazz!!, text)
    }

    fun <T> toBean(schema: Schema, clazz: Class<T>, text: String): T? {
        logger.debug("Deserialize class:${clazz} from text:${text}")
        val datumReader: DatumReader<T?> = SpecificDatumReader(clazz)
        val decoder: Decoder = DecoderFactory.get().jsonDecoder(schema, text)
        val record = datumReader.read(null, decoder)
        logger.debug("Deserialize successful:${record.toString()}")
        return record
    }
}