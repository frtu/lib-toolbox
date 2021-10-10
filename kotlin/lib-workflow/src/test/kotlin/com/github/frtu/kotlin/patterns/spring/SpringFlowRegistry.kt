package com.github.frtu.kotlin.patterns.spring

import com.github.frtu.kotlin.flow.model.SampleFlow
import com.github.frtu.kotlin.patterns.AbstractRegistry
import org.springframework.stereotype.Component

/**
 * A sample registry populated by spring
 * @author Frédéric TU
 * @since 1.1.4
 */
@Component
class SpringFlowRegistry(registry: MutableMap<String, SampleFlow>) :
    AbstractRegistry<String, SampleFlow>("flow", registry)