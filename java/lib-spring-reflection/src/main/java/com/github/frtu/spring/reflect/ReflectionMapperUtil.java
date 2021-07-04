package com.github.frtu.spring.reflect;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Scan a source Class or Method and map it into a targetClass where fields has the wanted annotation.
 *
 * @author Frédéric TU
 * @see <a href="https://github.com/frtu/SimpleToolbox/blob/master/SimpleInfra/src/main/java/com/github/frtu/simple/infra/reflect/ReflectionMapperUtil.java">Moved from old project SimpleToolbox</a>
 * @since 1.0.2
 */
public class ReflectionMapperUtil {
    public static <T> T scanAnnotationSource(final Class<?> sourceClass, final Class<T> targetClass) {
        final T bean = BeanGenerator.generateBean(targetClass);

        FieldCallback fc = new FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                Class<?> type = field.getType();
                // If is annotation
                if (type.isAnnotation()) {
                    Class<? extends Annotation> annotation = type.asSubclass(Annotation.class);
                    // Fetch the annotation from the sourceClass
                    Annotation annotationFoundInSource = AnnotationUtils.findAnnotation(sourceClass, annotation);
                    // Set it back into the target object
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, bean, annotationFoundInSource);
                }
            }
        };
        ReflectionUtils.doWithFields(targetClass, fc);
        return bean;
    }

    public static <T> T scanAnnotationSource(final Method sourceMethod, final Class<T> targetClass) {
        final T bean = BeanGenerator.generateBean(targetClass);

        FieldCallback fc = new FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                Class<?> type = field.getType();
                // If is annotation
                if (type.isAnnotation()) {
                    Class<? extends Annotation> annotation = type.asSubclass(Annotation.class);
                    // Fetch the annotation from the sourceClass
                    Annotation annotationFoundInSource = AnnotationUtils.findAnnotation(sourceMethod, annotation);
                    // Set it back into the target object
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, bean, annotationFoundInSource);
                }
            }
        };
        ReflectionUtils.doWithFields(targetClass, fc);
        return bean;
    }
}
