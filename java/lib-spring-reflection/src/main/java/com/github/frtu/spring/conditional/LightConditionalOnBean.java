package com.github.frtu.spring.conditional;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Conditional that only matches when beans meeting all the specified requirements are already contained
 * in the BeanFactory. All the requirements must be met for the condition to match, but they do not have
 * to be met by the same bean.
 * <p>
 * Similar to Spring-Boot annotation without spring-boot dependencies {link org.springframework.boot.autoconfigure.condition.ConditionalOnBean}
 * </p>
 *
 * @author Frédéric TU
 * @see <a href="https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/autoconfigure/condition/ConditionalOnBean.html">Spring-Boot annotation javadoc</a>
 * @since 1.0.1
 */
public class LightConditionalOnBean implements Condition {
    private final String beanName;

    public LightConditionalOnBean(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return conditionContext.getRegistry().containsBeanDefinition(this.beanName);
    }
}
