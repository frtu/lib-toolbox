package com.github.frtu.spring.conditional;

import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Conditional  that only matches when the specified classes are not on the classpath.
 * <p>
 * Similar to Spring-Boot annotation without spring-boot dependencies {link org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass}
 * </p>
 *
 * @author Frédéric TU
 * @see <a href="https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/autoconfigure/condition/ConditionalOnMissingClass.html">Spring-Boot annotation javadoc</a>
 * @since 1.0.1
 */
public class LightConditionalOnMissingClass extends LightConditionalOnClass {
    public LightConditionalOnMissingClass(String className) {
        super(className);
    }

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return !super.matches(conditionContext, annotatedTypeMetadata);
    }
}
