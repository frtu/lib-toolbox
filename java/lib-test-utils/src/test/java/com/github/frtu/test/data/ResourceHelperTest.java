package com.github.frtu.test.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ResourceHelperTest {
    @Test
    void readFromFile() {
        final String jsonSampleFromString = new ResourceHelper().readFromFile("classpath:dummy-bean.json");
        Assertions.assertEquals("{\"id\":\"id\",\"name\":\"name\"}", jsonSampleFromString.trim());
    }
}