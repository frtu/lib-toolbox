package com.github.frtu.kotlin.translate

import com.fasterxml.jackson.databind.JsonNode

/**
 * Transform from one type to another
 */
interface TextToJsonNodeTranslator : Translator<String, JsonNode>