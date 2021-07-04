package com.github.frtu.spring.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class ClassPathScanningAnnotationProviderTest {
    /**
     * Very slow. Uncomment when want to test
     */
//    @Test
    public void findCandidateComponents() {
        final Set<BeanDefinition> candidateComponents = ClassPathScanningAnnotationProvider.findCandidateComponents(Configuration.class);
        assertTrue(candidateComponents.stream().anyMatch(def -> "com.github.frtu.spring.conditional.DummyBeanConfiguration".equals(def.getBeanClassName())));
        assertTrue(candidateComponents.stream().anyMatch(def -> "com.github.frtu.logs.tracing.annotation.ExecutionSpanConfiguration".equals(def.getBeanClassName())));
    }

    /**
     * Very slow. Uncomment when want to test
     */
//    @Test
    public void findCandidateComponentsByBasePackage() {
        final Set<BeanDefinition> candidateComponents = ClassPathScanningAnnotationProvider.findCandidateComponents(Configuration.class, "com.github.frtu.spring.conditional");
        final int size = candidateComponents.size();
        assertEquals(1, size);
        assertTrue(candidateComponents.stream().anyMatch(def -> "com.github.frtu.spring.conditional.DummyBeanConfiguration".equals(def.getBeanClassName())));
    }
}