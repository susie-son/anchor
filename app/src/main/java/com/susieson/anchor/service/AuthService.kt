package com.susieson.anchor.service

import com.google.firebase.auth.FirebaseAuth
import com.susieson.anchor.model.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface AuthService {
    val currentUser: Flow<User?>
    suspend fun createAnonymousAccount()
}

class AuthServiceImpl @Inject constructor(private val auth: FirebaseAuth) : AuthService {
    override val currentUser: Flow<User?>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    auth.currentUser?.let {
                        trySend(User(it.uid))
                    } ?: trySend(null)
                }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }

    override suspend fun createAnonymousAccount() {
        auth.signInAnonymously().await()
    }
}