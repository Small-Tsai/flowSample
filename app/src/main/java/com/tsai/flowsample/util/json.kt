package com.tsai.flowsample.util

import kotlinx.serialization.json.Json

val json = Json {
    isLenient = true
    ignoreUnknownKeys = true
    allowSpecialFloatingPointValues = true
    useArrayPolymorphism = true
    encodeDefaults = false
}