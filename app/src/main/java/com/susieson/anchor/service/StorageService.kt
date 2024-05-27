package com.susieson.anchor.service

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.firestore
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.model.Review
import com.susieson.anchor.model.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface StorageService {
    val exposures: Flow<List<Exposure>>
    suspend fun add(exposure: Exposure)
    suspend fun add(exposureId: String, review: Review)
}

class StorageServiceImpl @Inject constructor(
    private val auth: AuthService,
    private val firestore: FirebaseFirestore = Firebase.firestore
) : StorageService {
    @OptIn(ExperimentalCoroutinesApi::class)
    override val exposures: Flow<List<Exposure>>
        get() = auth.currentUser.flatMapLatest { user ->
            firestore
                .collection(USERS_COLLECTION)
                .document(user.id)
                .collection(EXPOSURES_COLLECTION)
                .orderBy(CREATED_AT_FIELD, Query.Direction.DESCENDING)
                .dataObjects()
        }

    override suspend fun add(exposure: Exposure) {
        val user = auth.currentUser.first()
        val document = firestore
            .collection(USERS_COLLECTION)
            .document(user.id)
            .collection(EXPOSURES_COLLECTION)
            .add(exposure)
            .await()
        val exposureWithId = exposure.copy(id = document.id)
        document.set(exposureWithId)
    }

    override suspend fun add(exposureId: String, review: Review) {
        val user = auth.currentUser.first()
        firestore
            .collection(USERS_COLLECTION)
            .document(user.id)
            .collection(EXPOSURES_COLLECTION)
            .document(exposureId)
            .update(REVIEW_FIELD, review, STATUS_FIELD, Status.COMPLETED)
    }

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val EXPOSURES_COLLECTION = "exposures"
        private const val CREATED_AT_FIELD = "createdAt"
        private const val REVIEW_FIELD = "review"
        private const val STATUS_FIELD = "status"
    }
}