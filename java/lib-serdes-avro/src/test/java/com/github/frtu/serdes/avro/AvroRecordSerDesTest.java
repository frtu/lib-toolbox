package com.github.frtu.serdes.avro;

import com.github.frtu.serdes.avro.json.JsonAvroSerdes;
import com.github.frtu.serdes.avro.generic.GenericRecordSerdesFactory;
import com.github.frtu.serdes.avro.specific.SpecificRecordSerdesFactory;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class AvroRecordSerDesTest {

    private static SpecificRecordSerdesFactory<DummyData> specificRecordSerdesFactory = new SpecificRecordSerdesFactory(DummyData.getClassSchema());
    private static GenericRecordSerdesFactory genericRecordSerdesFactory = new GenericRecordSerdesFactory(DummyData.getClassSchema());

    @Test
    public void specificSerdes() throws IOException {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        DummyData dummyData = DummyData.newBuilder().setName("Fred").build();

        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        AvroRecordSerializer<DummyData> specificRecordSerializer = specificRecordSerdesFactory.buildSerializer();
        byte[] serializedBytes = specificRecordSerializer.serialize(dummyData);

        AvroRecordDeserializer<DummyData> specificRecordDeserializer = specificRecordSerdesFactory.buildDeserializer();
        DummyData resultDummyData = specificRecordDeserializer.deserialize(serializedBytes);

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        Assert.assertEquals(dummyData.toString(), resultDummyData.toString());
    }

    @Test
    public void specificSerdesJSON() throws IOException {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        DummyData dummyData = DummyData.newBuilder().setName("Fred").build();

        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        AvroRecordSerializer<DummyData> specificRecordSerializer = specificRecordSerdesFactory.buildSerializer(true);
        byte[] serializedBytes = specificRecordSerializer.serialize(dummyData);

        AvroRecordDeserializer<DummyData> specificRecordDeserializer = specificRecordSerdesFactory.buildDeserializer(true);
        DummyData resultDummyData = specificRecordDeserializer.deserialize(serializedBytes);

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        Assert.assertEquals(dummyData.toString(), resultDummyData.toString());
    }

    @Test
    public void genericSerdes() throws IOException {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        GenericRecord dummyData = new GenericData.Record(DummyData.getClassSchema());
        dummyData.put("name", "Fred");

        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        AvroRecordSerializer<GenericRecord> genericRecordSerializer = genericRecordSerdesFactory.buildSerializer();
        byte[] serializedBytes = genericRecordSerializer.serialize(dummyData);

        AvroRecordDeserializer<GenericRecord> genericRecordDeserializer = genericRecordSerdesFactory.buildDeserializer();
        GenericRecord resultDummyData = genericRecordDeserializer.deserialize(serializedBytes);

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        Assert.assertEquals(dummyData.toString(), resultDummyData.toString());
    }

    @Test
    public void genericSerdesJSON() throws IOException {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        GenericRecord dummyData = new GenericData.Record(DummyData.getClassSchema());
        dummyData.put("name", "Fred");

        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        AvroRecordSerializer<GenericRecord> genericRecordSerializer = genericRecordSerdesFactory.buildSerializer(true);
        byte[] serializedBytes = genericRecordSerializer.serialize(dummyData);

        AvroRecordDeserializer<GenericRecord> genericRecordDeserializer = genericRecordSerdesFactory.buildDeserializer(true);
        GenericRecord resultDummyData = genericRecordDeserializer.deserialize(serializedBytes);

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        Assert.assertEquals(dummyData.toString(), resultDummyData.toString());
    }

    @Test
    public void crossSpecToGenSerdes() throws IOException {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        DummyData dummyData = DummyData.newBuilder().setName("Fred").build();

        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        AvroRecordSerializer<DummyData> specificRecordSerializer = specificRecordSerdesFactory.buildSerializer();
        byte[] serializedBytes = specificRecordSerializer.serialize(dummyData);

        AvroRecordDeserializer<GenericRecord> genericRecordDeserializer = genericRecordSerdesFactory.buildDeserializer();
        GenericRecord resultDummyData = genericRecordDeserializer.deserialize(serializedBytes);

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        Assert.assertEquals(dummyData.toString(), resultDummyData.toString());
    }

    @Test
    public void crossSpecToGenSerdesJSON() throws IOException {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        DummyData dummyData = DummyData.newBuilder().setName("Fred").build();

        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        AvroRecordSerializer<DummyData> specificRecordSerializer = specificRecordSerdesFactory.buildSerializer(true);
        byte[] serializedBytes = specificRecordSerializer.serialize(dummyData);

        AvroRecordDeserializer<GenericRecord> genericRecordDeserializer = genericRecordSerdesFactory.buildDeserializer(true);
        GenericRecord resultDummyData = genericRecordDeserializer.deserialize(serializedBytes);

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        Assert.assertEquals(dummyData.toString(), resultDummyData.toString());
    }

    @Test
    public void crossGenToSpecSerdes() throws IOException {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        GenericRecord dummyData = new GenericData.Record(DummyData.getClassSchema());
        dummyData.put("name", "Fred");

        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        AvroRecordSerializer<GenericRecord> genericRecordSerializer = genericRecordSerdesFactory.buildSerializer();
        byte[] serializedBytes = genericRecordSerializer.serialize(dummyData);

        AvroRecordDeserializer<DummyData> specificRecordDeserializer = specificRecordSerdesFactory.buildDeserializer();
        DummyData resultDummyData = specificRecordDeserializer.deserialize(serializedBytes);

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        Assert.assertEquals(dummyData.toString(), resultDummyData.toString());
    }

    @Test
    public void crossGenToSpecSerdesJSON() throws IOException {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        GenericRecord dummyData = new GenericData.Record(DummyData.getClassSchema());
        dummyData.put("name", "Fred");

        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        AvroRecordSerializer<GenericRecord> genericRecordSerializer = genericRecordSerdesFactory.buildSerializer(true);
        byte[] serializedBytes = genericRecordSerializer.serialize(dummyData);

        AvroRecordDeserializer<DummyData> specificRecordDeserializer = specificRecordSerdesFactory.buildDeserializer(true);
        DummyData resultDummyData = specificRecordDeserializer.deserialize(serializedBytes);

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        Assert.assertEquals(dummyData.toString(), resultDummyData.toString());
    }

    @Test
    public void bytesToJson() throws IOException {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        GenericRecord dummyData = new GenericData.Record(genericRecordSerdesFactory.getSchema());
        dummyData.put("name", "Fred");

        AvroRecordSerializer<GenericRecord> genericRecordSerializer = genericRecordSerdesFactory.buildSerializer();
        byte[] inputBytes = genericRecordSerializer.serialize(dummyData);
        final String expected = StringUtils.deleteWhitespace(dummyData.toString());

        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        final JsonAvroSerdes jsonAvroSerdes = genericRecordSerdesFactory.buildConverter();
        final String jsonString = jsonAvroSerdes.deserialize(inputBytes);

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        Assert.assertEquals(expected, StringUtils.deleteWhitespace(jsonString));
    }

    @Test
    public void jsonToBytes() throws IOException {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        GenericRecord dummyData = new GenericData.Record(genericRecordSerdesFactory.getSchema());
        dummyData.put("name", "Fred");

        AvroRecordSerializer<GenericRecord> genericRecordSerializer = genericRecordSerdesFactory.buildSerializer();
        final String inputJsonString = StringUtils.deleteWhitespace(dummyData.toString());
        byte[] expected = genericRecordSerializer.serialize(dummyData);

        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        final JsonAvroSerdes jsonAvroSerdes = genericRecordSerdesFactory.buildConverter();
        final byte[] resultBytes = jsonAvroSerdes.serialize(inputJsonString);

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        Assert.assertEquals(expected.length, resultBytes.length);
        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(expected[i], resultBytes[i]);
        }
    }
}