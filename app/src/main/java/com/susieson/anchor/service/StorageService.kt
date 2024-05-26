package com.susieson.anchor.service

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.firestore
import com.susieson.anchor.model.Voyage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface StorageService {
    val voyages: Flow<List<Voyage>>

    suspend fun add(voyage: Voyage)
}

class StorageServiceImpl @Inject constructor(
    private val auth: AuthService,
    private val firestore: FirebaseFirestore = Firebase.firestore
) : StorageService {
    @OptIn(ExperimentalCoroutinesApi::class)
    override val voyages: Flow<List<Voyage>>
        get() = auth.currentUser.flatMapLatest { user ->
            firestore
                .collection(USERS_COLLECTION)
                .document(user.id)
                .collection(VOYAGES_COLLECTION)
                .orderBy(CREATED_AT_FIELD, Query.Direction.DESCENDING)
                .dataObjects()
        }

    override suspend fun add(voyage: Voyage) {
        auth.currentUser.collect { user ->
            firestore
                .collection(USERS_COLLECTION)
                .document(user.id)
                .collection(VOYAGES_COLLECTION)
                .add(voyage)
                .await()
        }
    }

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val VOYAGES_COLLECTION = "voyages"
        private const val CREATED_AT_FIELD = "createdAt"
    }
}