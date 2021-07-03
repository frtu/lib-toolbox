package com.github.frtu.serdes.avro.specific;

import com.github.frtu.serdes.avro.AvroRecordSerializer;
import org.apache.avro.Schema;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;

/**
 * An Avro record serializer for POJO
 *
 * @param <T> The specific Avro class it is meant to serialize
 * @author frtu
 * @since 0.3.5
 */
public class SpecificRecordSerializer<T extends SpecificRecord> extends AvroRecordSerializer<T> {

    public SpecificRecordSerializer() {
        super();
    }

    public SpecificRecordSerializer(boolean isFormatJson) {
        super(isFormatJson);
    }

    @Override
    protected DatumWriter<T> buildDatumWriter(Schema schema) {
        return new SpecificDatumWriter<>(schema);
    }
}
