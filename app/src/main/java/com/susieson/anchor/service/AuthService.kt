package com.susieson.anchor.service

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface AuthService {
    suspend fun createAnonymousAccount()
}

class AuthServiceImpl @Inject constructor(): AuthService {
    override suspend fun createAnonymousAccount() {
        Firebase.auth.signInAnonymously().await()
    }
}