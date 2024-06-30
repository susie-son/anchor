package com.susieson.anchor.model

import com.google.firebase.auth.FirebaseUser

data class User(val id: String, val isAnonymous: Boolean, val email: String? = null)

fun FirebaseUser?.toUser() = this?.let { User(it.uid, it.isAnonymous, it.email) }
