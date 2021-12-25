package com.github.frtu.kotlin.patterns.spring

import com.github.frtu.kotlin.flow.model.SampleFlow

class GoodSampleFlow : SampleFlow(name = FLOW_NAME, shouldSucceed = false) {
    companion object {
        const val FLOW_NAME = "good-flow"
    }
}