package com.github.frtu.spring.conditional;

import org.junit.Test;
import org.springframework.context.annotation.Condition;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LightConditionalOnMissingClassTest {
    @Test
    public void matchesExisting() {
        Condition conditionalOnClass = new LightConditionalOnMissingClass(String.class.getCanonicalName());
        assertFalse(conditionalOnClass.matches(null, null));
    }

    @Test
    public void matchesNonExisting() {
        Condition conditionalOnClass = new LightConditionalOnMissingClass("nonExistingClass");
        assertTrue(conditionalOnClass.matches(null, null));
    }
}