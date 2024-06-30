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
    val currentUser: Flow<User?>
    suspend fun createAnonymousAccount()
    suspend fun signInWithEmailAndPassword(email: String, password: String)
    fun signOut()
    suspend fun deleteAccount()
    suspend fun reauthenticateWithEmailAndPassword(email: String, password: String)
    suspend fun linkAccountWithEmailAndPassword(email: String, password: String)
}

class AuthServiceImpl @Inject constructor(private val auth: FirebaseAuth) : AuthService {
    override val currentUser: Flow<User?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.toUser())
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose { auth.removeAuthStateListener(authStateListener) }
    }

    override suspend fun createAnonymousAccount() {
        auth.signInAnonymously().await()
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override fun signOut() {
        auth.signOut()
    }

    override suspend fun deleteAccount() {
        val user = checkNotNull(auth.currentUser) { "cannot delete account: user is unauthenticated" }
        user.delete().await()
    }

    override suspend fun reauthenticateWithEmailAndPassword(email: String, password: String) {
        val user = checkNotNull(auth.currentUser) { "cannot reauthenticate: user is unauthenticated" }
        val credential = EmailAuthProvider.getCredential(email, password)
        user.reauthenticate(credential).await()
    }

    override suspend fun linkAccountWithEmailAndPassword(email: String, password: String) {
        val user = checkNotNull(auth.currentUser) { "cannot link account: user is unauthenticated" }
        val credential = EmailAuthProvider.getCredential(email, password)
        user.linkWithCredential(credential).await()
    }
}
