package com.susieson.anchor.model

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
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
    val createdAt: Timestamp? = null,
    @ServerTimestamp
    @Serializable(with = TimestampSerializer::class)
    val updatedAt: Timestamp? = null,
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val preparation: Preparation? = null,
    val review: Review? = null,
    val status: Status = Status.DRAFT
) : Parcelable

val ExposureType = object : NavType<Exposure>(
    isNullableAllowed = false
) {
    override fun get(bundle: Bundle, key: String): Exposure? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return bundle.getParcelable(key, Exposure::class.java)
        } else {
            @Suppress("DEPRECATION")
            return bundle.getParcelable(key)
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
        val seconds = value.seconds
        val nanoseconds = value.nanoseconds
        encoder.encodeString("$seconds:$nanoseconds")
    }

    override fun deserialize(decoder: Decoder): Timestamp {
        val timestampString = decoder.decodeString()
        val parts = timestampString.split(":")
        val seconds = parts[0].toLong()
        val nanoseconds = parts[1].toInt()
        return Timestamp(seconds, nanoseconds)
    }
}

enum class Status {
    DRAFT,
    READY,
    IN_PROGRESS,
    COMPLETED
}
