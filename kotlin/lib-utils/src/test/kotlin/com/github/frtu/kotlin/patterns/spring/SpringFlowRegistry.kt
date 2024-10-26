package com.github.frtu.kotlin.patterns.spring

import com.github.frtu.kotlin.patterns.spring.model.SampleFlow
import com.github.frtu.kotlin.patterns.AbstractRegistryStringKeys
import org.springframework.stereotype.Component

/**
 * A sample registry populated by spring
 * @author Frédéric TU
 * @since 1.1.4
 */
@Component
class SpringFlowRegistry(registry: MutableMap<String, SampleFlow>) :
    AbstractRegistryStringKeys<SampleFlow>("flow", registry, SPRING_NAMESPACE) {

    companion object {
        /** Best practices - Use namespace to avoid spring beans name collision */
        const val SPRING_NAMESPACE = "spring-namespace-"
    }
}