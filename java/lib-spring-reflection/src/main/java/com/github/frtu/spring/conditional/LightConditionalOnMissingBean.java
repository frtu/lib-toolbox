package com.github.frtu.spring.conditional;

import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Conditional that only matches when no beans meeting the specified requirements are already contained in
 * the BeanFactory. None of the requirements must be met for the condition to match and the requirements
 * do not have to be met by the same bean.
 * <p>
 * Similar to Spring-Boot annotation without spring-boot dependencies {link org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean}
 * </p>
 *
 * @author Frédéric TU
 * @see <a href="https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/autoconfigure/condition/ConditionalOnMissingBean.html">Spring-Boot annotation javadoc</a>
 * @since 1.0.1
 */
public class LightConditionalOnMissingBean extends LightConditionalOnBean {
    public LightConditionalOnMissingBean(String beanName) {
        super(beanName);
    }

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return !super.matches(conditionContext, annotatedTypeMetadata);
    }
}
