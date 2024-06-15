package com.susieson.anchor.model

import com.google.firebase.auth.FirebaseUser

data class AnchorUser(
    val id: String,
    val isAnonymous: Boolean
)

fun FirebaseUser?.toAnchorUser(): AnchorUser? {
    if (this == null) return null
    return AnchorUser(
        id = uid,
        isAnonymous = isAnonymous
    )
}
