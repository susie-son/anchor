package com.susieson.anchor.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class Exposure(
    @ServerTimestamp
    val createdAt: Timestamp? = null,
    @ServerTimestamp
    val updatedAt: Timestamp? = null,
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val preparation: Preparation? = null,
    val review: Review? = null,
    val status: Status = Status.DRAFT
)

enum class Status {
    DRAFT,
    READY,
    IN_PROGRESS,
    COMPLETED
}
