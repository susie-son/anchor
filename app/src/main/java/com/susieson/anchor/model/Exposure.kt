package com.susieson.anchor.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class Exposure(
    @ServerTimestamp
    val createdAt: Timestamp? = null,
    val title: String = "",
    val description: String = "",
    val thoughts: List<String> = emptyList(),
    val interpretations: List<String> = emptyList(),
    val behaviors: List<String> = emptyList(),
    val actions: List<String> = emptyList(),
    val status: Status = Status.DRAFT
)

enum class Status {
    DRAFT,
    IN_PROGRESS,
    COMPLETED
}