package com.susieson.anchor.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class Review(
    @ServerTimestamp
    val createdAt: Timestamp? = null,
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
