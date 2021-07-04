package com.github.frtu.spring.conditional;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Verify the presence of a {@link Class} in the {@link ClassLoader}.
 * <p>
 * Lightweight version of org.springframework.boot.autoconfigure.condition.ConditionalOnClass,
 * without spring-boot.
 * </p>
 *
 * @author Frédéric TU
 * @see <a href="https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/autoconfigure/condition/ConditionalOnClass.html">Spring-Boot annotation javadoc</a>
 * @since 1.0.1
 */
public class LightConditionalOnClass implements Condition {
    private final String className;

    public LightConditionalOnClass(String className) {
        this.className = className;
    }

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
