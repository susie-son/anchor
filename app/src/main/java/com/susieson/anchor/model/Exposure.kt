package com.susieson.anchor.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class Exposure(
    @ServerTimestamp
    val createdAt: Timestamp? = null,
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val preparation: Preparation = Preparation(),
    val review: Review = Review(),
    val status: Status = Status.DRAFT
)

enum class Status {
    DRAFT,
    IN_PROGRESS,
    COMPLETED
}