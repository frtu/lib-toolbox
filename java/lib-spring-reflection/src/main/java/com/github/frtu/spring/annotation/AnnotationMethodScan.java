package com.github.frtu.spring.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * Result of an annotation method scan {@link AnnotationMethodScanner#scan(Method)}.
 *
 * @param <MethodAnno> Annotation class used on method
 * @param <ParamAnno>  Annotation class used on method parameter
 * @author Frédéric TU
 * @see <a href="https://github.com/frtu/log-platform/blob/v0.9.4/logger-libraries/logger-core/src/main/java/com/github/frtu/utils/AnnotationMethodScan.java">Moved from log-platform project</a>
 * @since 1.0.1
 */
public class AnnotationMethodScan<MethodAnno extends Class<? extends Annotation>, ParamAnno extends Class<? extends Annotation>> {
    public final static AnnotationMethodScan EMPTY = new AnnotationMethodScan(null, null, null);

    private Method method;
    private Map<String, Object> annotationAttributes;
    private Annotation[] paramAnnotation;

    AnnotationMethodScan(Method method, Map<String, Object> annotationAttributes, Annotation[] paramAnnotation) {
        this.method = method;
        this.annotationAttributes = annotationAttributes;
        this.paramAnnotation = paramAnnotation;
    }

    /**
     * The scanned {@link Method}.
     *
     * @return method scanned
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Raw fields contained in the method annotation.
     *
     * @return KV of all the annotation fields
     */
    public Map<String, Object> getAnnotationAttributes() {
        return annotationAttributes;
    }

    /**
     * Shortcut for annotation field 'value' that returns an array of annotations.
     *
     * @param <NestedAnno> Nested annotation type for 'value'
     * @return all the annotations object
     */
    public <NestedAnno extends Annotation> NestedAnno[] getAnnotationValueArray() {
        final NestedAnno[] annotationArray = (NestedAnno[]) annotationAttributes.get("value");
        return annotationArray;
    }

    /**
     * Array of annotations from each fields.
     * <p>
     * ATTENTION : ParamAnno[i] can be null if parameter index i doesn't have annotation.
     *
     * @param <ParamAnno> Annotation class used on method parameter
     * @return Empty array when no annotation found
     */
    public <ParamAnno extends Annotation> ParamAnno[] getParamAnnotations() {
        return (ParamAnno[]) paramAnnotation;
    }

    /**
     * Is AnnotationMethodScan contains nothing.
     *
     * @return if contains nothing
     * @author Frédéric TU
     * @since 1.0.2
     */
    public boolean isEmpty() {
        return isEmpty(this);
    }

    /**
     * Is AnnotationMethodScan contains nothing.
     *
     * @param annotationMethodScan an {@link AnnotationMethodScan}
     * @return if contains nothing
     * @author Frédéric TU
     * @since 1.0.2
     */
    public static boolean isEmpty(AnnotationMethodScan annotationMethodScan) {
        return AnnotationMethodScan.EMPTY.equals(annotationMethodScan);
    }

    @Override
    public String toString() {
        return "AnnotationMethodScan{" +
                "method=" + method +
                ", annotationAttributes=" + annotationAttributes +
                ", paramAnnotation=" + Arrays.toString(paramAnnotation) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnotationMethodScan<?, ?> that = (AnnotationMethodScan<?, ?>) o;
        return Objects.equals(method, that.method) &&
                Objects.equals(annotationAttributes, that.annotationAttributes) &&
                Arrays.equals(paramAnnotation, that.paramAnnotation);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(method, annotationAttributes);
        result = 31 * result + Arrays.hashCode(paramAnnotation);
        return result;
    }
}
