package com.github.frtu.spring.conditional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.github.frtu.spring.conditional.DummyBeanConfiguration.BASE_DUMMY_BEAN;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DummyBeanConfiguration.class})
public class LightConditionalOnBeanTest {
    @Autowired
    DummyBeanConfiguration.DummyBean dummyBean;

    public static class DummyBeanLightConditionalOnBean extends LightConditionalOnBean {
        public DummyBeanLightConditionalOnBean() {
            super(BASE_DUMMY_BEAN);
        }
    }

    @Test
    public void matches() {
        assertEquals(BASE_DUMMY_BEAN, dummyBean.getName());
    }
}