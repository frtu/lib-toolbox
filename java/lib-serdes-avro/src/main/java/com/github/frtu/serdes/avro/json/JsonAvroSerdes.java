package com.github.frtu.serdes.avro.json;

import com.github.frtu.serdes.avro.AvroRecordDeserializer;
import com.github.frtu.serdes.avro.generic.GenericRecordDeserializer;
import com.github.frtu.serdes.avro.generic.GenericRecordSerializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Allow to serialize and deserialize JSON type FROM and INTO Avro bytes.
 *
 * @param <T> The Avro type corresponding to the Avro schema for this JSON string.
 * @author frtu
 * @since 0.3.5
 */
public class JsonAvroSerdes<T extends GenericRecord> extends GenericRecordSerializer<T> {

    public static final String CHARSET_NAME = "UTF-8";

    private AvroRecordDeserializer<T> avroRecordDeserializer;

    public JsonAvroSerdes(Schema schema) {
        this(new GenericRecordDeserializer<>(schema));
    }

    public JsonAvroSerdes(GenericRecordDeserializer<T> avroRecordDeserializer) {
        super(true);
        this.avroRecordDeserializer = avroRecordDeserializer;
    }

    /**
     * Deserialize the Avro bytes of type {@link T} into JSON string.
     *
     * @param bytes the Avro bytes with the same schema as the expected JSON
     * @return JSON String corresponding to the avro object.
     * @throws IOException bytes is not matching the expectation
     */
    public String deserialize(byte[] bytes) throws IOException {
        final T avroObject = avroRecordDeserializer.deserialize(bytes, false);
        final byte[] serializeJson = super.serialize(avroObject, true);
        return new String(serializeJson, Charset.forName(CHARSET_NAME));
    }

    /**
     * Serialize the JSON string into Avro bytes of type {@link T}.
     *
     * @param jsonString the JSON representation of the Avro object
     * @return the Avro bytes with the same schema as the expected JSON.
     * @throws IOException the json string is not matching the expectation
     */
    public byte[] serialize(String jsonString) throws IOException {
        final T avroObject = avroRecordDeserializer.deserialize(jsonString.getBytes(CHARSET_NAME), true);
        final byte[] serializeAvro = super.serialize(avroObject, false);
        return serializeAvro;
    }
}
