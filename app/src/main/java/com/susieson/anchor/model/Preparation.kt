package com.susieson.anchor.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Preparation(
    val thoughts: List<String> = emptyList(),
    val interpretations: List<String> = emptyList(),
    val behaviors: List<String> = emptyList(),
    val actions: List<String> = emptyList()
) : Parcelable
