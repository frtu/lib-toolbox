package com.github.frtu.spring.reflect;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BeanGeneratorTest {
    public static class DummyBean {
        private String name;

        public String getName() {
            return name;
        }
    }

    @Test
    public void generatePopulatedBean() {
        final DummyBean dummyBean = BeanGenerator.generatePopulatedBean(DummyBean.class);
        assertEquals("name", dummyBean.getName());
    }
}