package com.github.frtu.kotlin.patterns.spring

import com.github.frtu.kotlin.flow.model.SampleFlow
import com.github.frtu.kotlin.patterns.spring.ErrorSampleFlow.Companion.FLOW_NAME
import org.springframework.stereotype.Component

@Component(FLOW_NAME)
class ErrorSampleFlow : SampleFlow(name = FLOW_NAME, shouldSucceed = false) {
    companion object {
        const val FLOW_NAME = "error-flow"
    }
}