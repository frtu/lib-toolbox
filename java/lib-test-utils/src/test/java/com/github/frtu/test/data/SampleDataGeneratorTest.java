package com.github.frtu.test.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Frédéric TU
 * @see <a href="https://github.com/frtu/SimpleToolbox/blob/master/SimpleInfra/src/test/java/com/github/frtu/simple/infra/reflect/BeanGeneratorTest.java">Moved from old project SimpleToolbox</a>
 * @since 1.0.2
 */
public class SampleDataGeneratorTest {
    @Test
    public void testJSON() {
        String jsonSampleFromString = SampleDataGenerator.getJSONSampleFromString(TestBean.class);
        assertNotNull(jsonSampleFromString);
        assertEquals("{\"id\":\"id\",\"name\":\"name\"}", jsonSampleFromString);
    }

//    @Test
//    public void testXML() {
//        String xmlSampleFromString = SampleDataGenerator.getXMLSampleFromString(TestBean.class);
//        assertNotNull(xmlSampleFromString);
//        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><testBean><id>id</id><name>name</name></testBean>",
//                xmlSampleFromString);
//    }
}
