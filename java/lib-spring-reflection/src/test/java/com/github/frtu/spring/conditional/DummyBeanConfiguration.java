package com.github.frtu.spring.conditional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DummyBeanConfiguration {
    public static final String BASE_DUMMY_BEAN = "dummyBean";
    public static final String CONDITIONAL_DUMMY_BEAN = "dummyBeanConditional";

    public static class DummyBean {
        private String name;

        public DummyBean(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    @Bean(BASE_DUMMY_BEAN)
    public DummyBean dummyBean() {
        return new DummyBean(BASE_DUMMY_BEAN);
    }

    @Bean(CONDITIONAL_DUMMY_BEAN)
    @Conditional(LightConditionalOnBeanTest.DummyBeanLightConditionalOnBean.class)
    public DummyBean dummyBeanConditional() {
        return new DummyBean(CONDITIONAL_DUMMY_BEAN);
    }
}
