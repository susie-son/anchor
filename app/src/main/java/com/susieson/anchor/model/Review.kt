package com.susieson.anchor.model

import android.os.Parcelable
import androidx.annotation.StringRes
import com.susieson.anchor.R
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
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
) : Parcelable

enum class Emotion(@StringRes val label: Int) {
    FEAR(R.string.review_fear_chip),
    SADNESS(R.string.review_sadness_chip),
    ANXIETY(R.string.review_anxiety_chip),
    GUILT(R.string.review_guilt_chip),
    SHAME(R.string.review_shame_chip),
    HAPPINESS(R.string.review_happiness_chip)
}
