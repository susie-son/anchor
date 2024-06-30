package com.susieson.anchor.model

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.navigation.NavType
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import com.susieson.anchor.R
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json

@Serializable
@Parcelize
data class Exposure(
    @ServerTimestamp
    @Serializable(with = TimestampSerializer::class)
    val createdAt: Timestamp = Timestamp.now(),
    @ServerTimestamp
    @Serializable(with = TimestampSerializer::class)
    val updatedAt: Timestamp = Timestamp.now(),
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val preparation: Preparation = Preparation(),
    val review: Review = Review(),
    val status: Status = Status.DRAFT
) : Parcelable

object ExposureNavType : NavType<Exposure>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Exposure? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, Exposure::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): Exposure {
        return Json.decodeFromString<Exposure>(value)
    }

    override fun serializeAsValue(value: Exposure): String {
        return Json.encodeToString(value)
    }

    override fun put(bundle: Bundle, key: String, value: Exposure) {
        bundle.putParcelable(key, value)
    }
}

object TimestampSerializer : KSerializer<Timestamp> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Timestamp", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Timestamp) {
        encoder.encodeString("${value.seconds}:${value.nanoseconds}")
    }

    override fun deserialize(decoder: Decoder): Timestamp {
        val timestampString = decoder.decodeString()
        val (seconds, nanoseconds) = timestampString.split(":")
        return Timestamp(
            seconds.toLongOrNull() ?: 0,
            nanoseconds.toIntOrNull() ?: 0
        )
    }
}

enum class Status {
    DRAFT,
    READY,
    IN_PROGRESS,
    COMPLETED
}

@Serializable
@Parcelize
data class Preparation(
    val thoughts: List<String> = emptyList(),
    val interpretations: List<String> = emptyList(),
    val behaviors: List<String> = emptyList(),
    val actions: List<String> = emptyList()
) : Parcelable

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
    ANGER(R.string.review_anger_chip),
    HAPPINESS(R.string.review_happiness_chip)
}
