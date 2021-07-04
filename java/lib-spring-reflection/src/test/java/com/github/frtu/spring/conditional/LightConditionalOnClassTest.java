package com.github.frtu.spring.conditional;

import org.junit.Test;
import org.springframework.context.annotation.Condition;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LightConditionalOnClassTest {
    @Test
    public void matchesExisting() {
        Condition conditionalOnClass = new LightConditionalOnClass(String.class.getCanonicalName());
        assertTrue(conditionalOnClass.matches(null, null));
    }

    @Test
    public void matchesNonExisting() {
        Condition conditionalOnClass = new LightConditionalOnClass("nonExistingClass");
        assertFalse(conditionalOnClass.matches(null, null));
    }
}