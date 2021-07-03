package com.github.frtu.serdes.avro.generic;

import com.github.frtu.serdes.avro.AvroRecordSerializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;

/**
 * An Avro record serializer using a Avro Schema
 *
 * @param <T> The generic Avro class it is meant to serialize
 * @author frtu
 * @since 0.3.5
 */
public class GenericRecordSerializer<T extends GenericRecord> extends AvroRecordSerializer<T> {

    public GenericRecordSerializer() {
        super();
    }

    public GenericRecordSerializer(boolean isFormatJson) {
        super(isFormatJson);
    }

    @Override
    protected DatumWriter<T> buildDatumWriter(Schema schema) {
        return new GenericDatumWriter<>(schema);
    }
}
