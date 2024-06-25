package com.susieson.anchor.service

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.susieson.anchor.model.User
import com.susieson.anchor.model.toUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface AuthService {
    val user: Flow<User?>
    suspend fun createAnonymousAccount()
    suspend fun authenticate(email: String, password: String)
    suspend fun signOut()
    suspend fun deleteAccount()
    suspend fun reAuthenticate(email: String, password: String)
    suspend fun linkAccount(email: String, password: String)
}

class AuthServiceImpl
@Inject
constructor(private val auth: FirebaseAuth) : AuthService {
    override val user: Flow<User?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.toUser())
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

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun deleteAccount() {
        val user = checkNotNull(auth.currentUser) { "unauthenticated" }
        user.delete().await()
    }

    override suspend fun reAuthenticate(email: String, password: String) {
        val user = checkNotNull(auth.currentUser) { "unauthenticated" }
        val credential = EmailAuthProvider.getCredential(email, password)
        user.reauthenticate(credential).await()
    }

    override suspend fun linkAccount(email: String, password: String) {
        val user = checkNotNull(auth.currentUser) { "unauthenticated" }
        val credential = EmailAuthProvider.getCredential(email, password)
        user.linkWithCredential(credential).await()
    }
}
