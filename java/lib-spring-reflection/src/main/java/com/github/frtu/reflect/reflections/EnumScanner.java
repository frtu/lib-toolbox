package com.github.frtu.reflect.reflections;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Scaner package for all Enums {@link #scan()} or {@link #scan(Class)}
 *
 * @param <E>
 * @author Frédéric TU
 * @since 1.0.5
 */
@Slf4j
public class EnumScanner<E extends Enum> {
    private Reflections reflections;

    public static <E extends Enum> EnumScanner of(Package aPackage) {
        return of(aPackage.getName());
    }

    public static <E extends Enum> EnumScanner of(String packageToScan) {
        LOGGER.trace("Create a Scanner for packageToScan='{}'", packageToScan);
        Reflections reflections = new Reflections(packageToScan);
        return new EnumScanner<>(reflections);
    }

    private EnumScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    /**
     * Scan all enum of sub package from {@link #of(String)}
     *
     * @return Set of enum classes
     */
    public Set<Class<? extends Enum>> scan() {
        return scan(Enum.class);
    }

    /**
     * Scan all enum implementing a specific Interface interfaceClass in sub package {@link #of(String)}
     * <p>
     * ATTENTION : All classes implementing interfaceClass MUST be of type {@link Enum}
     *
     * @param interfaceClass
     * @param <E>
     * @return Set of enum classes
     */
    public <E extends Enum> Set<Class<? extends E>> scan(Class<E> interfaceClass) {
        final Set<Class<? extends E>> subTypesOfInterface = reflections.getSubTypesOf(interfaceClass);
        final List<Class<? extends E>> classList = subTypesOfInterface.stream()
                .filter(enumClass -> !Enum.class.isAssignableFrom(enumClass))
                .collect(Collectors.toList());

        if (!classList.isEmpty()) {
            throw new IllegalArgumentException("ATTENTION : " + interfaceClass + " has implementation that are NOT enums :" + classList);
        }
        return subTypesOfInterface;
    }
}
