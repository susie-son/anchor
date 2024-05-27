package com.susieson.anchor.service

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.model.Review
import com.susieson.anchor.model.Status
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface StorageService {
    suspend fun add(exposure: Exposure)
    suspend fun add(exposureId: String, review: Review)
    suspend fun get(exposureId: String): Exposure
    suspend fun get(): List<Exposure>
}

class StorageServiceImpl @Inject constructor(
    private val auth: AuthService,
    private val firestore: FirebaseFirestore = Firebase.firestore
) : StorageService {
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
            .await()
    }

    override suspend fun get(exposureId: String): Exposure {
        val user = auth.currentUser.first()
        return firestore
            .collection(USERS_COLLECTION)
            .document(user.id)
            .collection(EXPOSURES_COLLECTION)
            .document(exposureId)
            .get()
            .await()
            .toObject(Exposure::class.java)!!
    }

    override suspend fun get(): List<Exposure> {
        val user = auth.currentUser.first()
        return firestore
            .collection(USERS_COLLECTION)
            .document(user.id)
            .collection(EXPOSURES_COLLECTION)
            .orderBy(CREATED_AT_FIELD, Query.Direction.DESCENDING)
            .get()
            .await()
            .toObjects(Exposure::class.java)
    }

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val EXPOSURES_COLLECTION = "exposures"
        private const val CREATED_AT_FIELD = "createdAt"
        private const val REVIEW_FIELD = "review"
        private const val STATUS_FIELD = "status"
    }
}