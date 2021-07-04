package com.github.frtu.spring.reflect;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import java.lang.reflect.Field;

/**
 * Generate a Bean that is public {@link #generateBean(Class)}.
 * <p>
 * Generate and populate String values using {@link #generatePopulatedBean(Class)}. String ONLY.
 *
 * @author Frédéric TU
 * @see <a href="https://github.com/frtu/SimpleToolbox/blob/master/SimpleInfra/src/main/java/com/github/frtu/simple/infra/reflect/BeanGenerator.java#L22-L56">Moved from old project SimpleToolbox</a>
 * @since 1.0.2
 */
public class BeanGenerator {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BeanGenerator.class);

    public static <T> T generateBean(Class<T> clazz) {
        T object;
        try {
            object = ConstructorUtils.invokeConstructor(clazz, (Object[]) null);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                    "The class passed MUST have an empty constructor OR not be an embedded class ! Class="
                            + clazz.getCanonicalName(), e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("The empty constructor MUST be public ! Class=" + clazz.getCanonicalName(), e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error in creating instance of class=" + clazz.getCanonicalName(), e);
        }
        return object;
    }

    public static <T> T generatePopulatedBean(Class<T> clazz) {
        final T object = generateBean(clazz);

        FieldCallback fc = new FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException {
                Class<?> classField = field.getType();
                // Assign String with its field name
                if (String.class.equals(classField)) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, object, field.getName());
                } else {
                    logger.error("Cannot set field '{}' that is from class '{}'!", field.getName(), classField);
                }
            }
        };
        ReflectionUtils.doWithFields(clazz, fc);
        return object;
    }
}
