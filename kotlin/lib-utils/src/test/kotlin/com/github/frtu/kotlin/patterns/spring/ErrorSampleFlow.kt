package com.github.frtu.kotlin.patterns.spring

import com.github.frtu.kotlin.patterns.spring.model.SampleFlow

class ErrorSampleFlow : SampleFlow(name = FLOW_NAME, shouldSucceed = false) {
    companion object {
        const val FLOW_NAME = "error-flow"
    }
}