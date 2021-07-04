package com.github.frtu.reflect;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * {@link Predicate} for {@link Field}.
 *
 * @author Frédéric TU
 * @since 1.0.5
 */
public class FieldPredicate {
    public static Predicate<Field> isEnumInnerField() {
        return field -> !field.getType().equals(field.getDeclaringClass()) && !EnumUtil.ALL_ENUM_FIELD_NAME.equals(field.getName());
    }

    public static class NameInArray implements Predicate<Field> {
        private List<String> fieldNames;

        public NameInArray(String... fieldNames) {
            this.fieldNames = Arrays.asList(fieldNames);
        }

        @Override
        public boolean test(Field field) {
            return fieldNames.contains(field.getName());
        }
    }
}
