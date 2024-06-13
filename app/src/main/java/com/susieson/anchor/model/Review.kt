package com.susieson.anchor.model

data class Review(
    val emotions: List<String> = emptyList(),
    val thoughts: List<String> = emptyList(),
    val sensations: List<String> = emptyList(),
    val behaviors: List<String> = emptyList(),
    val experiencing: Float = 0.0f,
    val anchoring: Float = 0.0f,
    val thinking: Float = 0.0f,
    val engaging: Float = 0.0f,
    val learnings: String = ""
)
