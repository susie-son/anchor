package com.susieson.anchor.service

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface AuthService {
    suspend fun getUserId(): String
}

class AuthServiceImpl @Inject constructor(private val auth: FirebaseAuth) : AuthService {

    override suspend fun getUserId(): String {
        auth.signInAnonymously().await()
        return auth.currentUser?.uid ?: throw IllegalStateException("User is not logged in")
    }
}