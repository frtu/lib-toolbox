package com.github.frtu.generators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.frtu.spring.reflect.BeanGenerator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Generate Bean object and populate data using field name.
 *
 * @author Frédéric TU
 * @see <a href="https://github.com/frtu/SimpleToolbox/blob/master/SimpleInfra/src/main/java/com/github/frtu/simple/infra/reflect/BeanGenerator.java#L58-L98">Moved from old project SimpleToolbox</a>
 * @since 1.0.2
 */
public class SampleDataGenerator extends BeanGenerator {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> String getJSONSampleFromString(Class<T> clazz) {
        return getJSONSampleFromString(clazz, false);
    }

    public static <T> String getJSONSampleFromString(Class<T> clazz, boolean prettyPrint) {
        T classSampleObj = generatePopulatedBean(clazz);
        try {
            String result;
            if (prettyPrint) {
                ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
                result = objectWriter.writeValueAsString(classSampleObj);
            } else {
                result = objectMapper.writeValueAsString(classSampleObj);
            }
            return result;
        } catch (IOException e) {
            throw new IllegalArgumentException("Impossible to serialize to JSON class=" + clazz.getCanonicalName(), e);
        }
    }

    public static <T> String getXMLSampleFromString(Class<T> clazz) {
        return getXMLSampleFromString(clazz, false);
    }

    public static <T> String getXMLSampleFromString(Class<T> clazz, boolean prettyPrint) {
        T classSampleObj = generatePopulatedBean(clazz);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            if (prettyPrint) {
                // output pretty printed
                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            }
            StringWriter stringWriter = new StringWriter();
            jaxbMarshaller.marshal(classSampleObj, stringWriter);

            return stringWriter.toString();
        } catch (JAXBException e) {
            throw new IllegalArgumentException("Impossible to serialize to XML class=" + clazz.getCanonicalName(), e);
        }
    }
}
