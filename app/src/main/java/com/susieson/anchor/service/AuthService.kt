package com.susieson.anchor.service

import com.google.firebase.auth.FirebaseAuth
import com.susieson.anchor.model.AnchorUser
import com.susieson.anchor.model.toAnchorUser
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

interface AuthService {
    val user: Flow<AnchorUser?>
    suspend fun createAnonymousAccount()
    suspend fun authenticate(email: String, password: String)
    suspend fun createAccount(email: String, password: String)
}

class AuthServiceImpl
@Inject
constructor(private val auth: FirebaseAuth) : AuthService {
    override val user: Flow<AnchorUser?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.toAnchorUser())
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose { auth.removeAuthStateListener(authStateListener) }
    }

    override suspend fun createAnonymousAccount() {
        auth.signInAnonymously().await()
    }

    override suspend fun authenticate(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
    }
}
