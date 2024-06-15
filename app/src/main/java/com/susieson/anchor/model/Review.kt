package com.susieson.anchor.model

data class Review(
    val emotions: List<Emotion> = emptyList(),
    val thoughts: List<String> = emptyList(),
    val sensations: List<String> = emptyList(),
    val behaviors: List<String> = emptyList(),
    val experiencing: Float = 0f,
    val anchoring: Float = 0f,
    val thinking: Float = 0f,
    val engaging: Float = 0f,
    val learnings: String = ""
)

enum class Emotion {
    FEAR,
    SADNESS,
    ANXIETY,
    GUILT,
    SHAME,
    HAPPINESS
}
