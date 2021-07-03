package com.github.frtu.serdes.avro.generic;

import com.github.frtu.serdes.avro.AvroRecordDeserializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;

/**
 * An Avro record deserializer using a Avro Schema
 *
 * @param <T> The generic Avro class it is meant to deserialize
 * @author frtu
 * @since 0.3.5
 */
public class GenericRecordDeserializer<T extends GenericRecord> extends AvroRecordDeserializer<T> {

    public GenericRecordDeserializer(Schema schema) {
        super(schema);
    }

    public GenericRecordDeserializer(Schema schema, boolean isFormatJson) {
        super(schema, isFormatJson);
    }

    @Override
    protected DatumReader<T> buildDatumReader() {
        return new GenericDatumReader<>(getSchema());
    }
}
