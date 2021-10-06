package com.github.frtu.kotlin.patterns

class UnrecognizedElementException(type: String, name: String) :
    IllegalStateException("No $type configured for $name")