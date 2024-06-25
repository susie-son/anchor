package com.susieson.anchor.model

import com.google.firebase.auth.FirebaseUser

data class User(
    val id: String,
    val isAnonymous: Boolean,
    val email: String? = null
)

fun FirebaseUser?.toUser(): User? {
    if (this == null) return null
    return User(
        id = uid,
        isAnonymous = isAnonymous,
        email = email
    )
}
