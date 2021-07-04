package com.github.frtu.spring.conditional.commons;

import com.github.frtu.spring.conditional.LightConditionalOnClass;

/**
 * Used with spring-core {link org.springframework.context.annotation.Conditional} annotation.
 * <p>
 * Check if aspectj is in the classpath.
 *
 * @author Frédéric TU
 * @since 1.0.2
 */
public class AopConditionalOnClass extends LightConditionalOnClass {
    private static final String ASPECT_CANONICAL_NAME = "org.aspectj.lang.annotation.Aspect";

    public AopConditionalOnClass() {
        super(ASPECT_CANONICAL_NAME);
    }
}
