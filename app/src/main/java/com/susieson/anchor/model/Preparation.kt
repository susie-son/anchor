package com.susieson.anchor.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class Preparation(
    @ServerTimestamp
    val createdAt: Timestamp? = null,
    val thoughts: List<String> = emptyList(),
    val interpretations: List<String> = emptyList(),
    val behaviors: List<String> = emptyList(),
    val actions: List<String> = emptyList(),
)
