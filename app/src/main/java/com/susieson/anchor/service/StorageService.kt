package com.susieson.anchor.service

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.model.Preparation
import com.susieson.anchor.model.Review
import com.susieson.anchor.model.Status
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface StorageService {
    suspend fun add(userId: String): String
    suspend fun add(userId: String, exposureId: String, title: String, description: String)
    suspend fun add(userId: String, exposureId: String, preparation: Preparation)
    suspend fun add(userId: String, exposureId: String, review: Review)
    suspend fun get(userId: String): List<Exposure>
    suspend fun get(userId: String, exposureId: String): Exposure
    suspend fun delete(userId: String, exposureId: String)
}

class StorageServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : StorageService {

    private fun exposuresCollectionRef(userId: String) =
        firestore.collection(USERS_COLLECTION)
            .document(userId)
            .collection(EXPOSURES_COLLECTION)

    private fun exposureDocumentRef(userId: String, exposureId: String) =
        exposuresCollectionRef(userId)
            .document(exposureId)

    override suspend fun add(userId: String): String {
        val exposure = Exposure()
        val document = exposuresCollectionRef(userId)
            .add(exposure)
            .await()
        val exposureWithId = exposure.copy(id = document.id)
        document.set(exposureWithId)
        return document.id
    }

    override suspend fun add(
        userId: String,
        exposureId: String,
        title: String,
        description: String
    ) {
        exposureDocumentRef(userId, exposureId)
            .update(
                TITLE_FIELD,
                title,
                DESCRIPTION_FIELD,
                description,
                STATUS_FIELD,
                Status.IN_PROGRESS
            )
            .await()
    }

    override suspend fun add(userId: String, exposureId: String, preparation: Preparation) {
        exposureDocumentRef(userId, exposureId)
            .update(PREPARATION_FIELD, preparation)
            .await()
    }

    override suspend fun add(userId: String, exposureId: String, review: Review) {
        exposureDocumentRef(userId, exposureId)
            .update(REVIEW_FIELD, review, STATUS_FIELD, Status.COMPLETED)
            .await()
    }

    override suspend fun get(userId: String): List<Exposure> {
        return exposuresCollectionRef(userId)
            .orderBy(CREATED_AT_FIELD, Query.Direction.DESCENDING)
            .get()
            .await()
            .toObjects(Exposure::class.java)
    }

    override suspend fun get(userId: String, exposureId: String): Exposure {
        return exposureDocumentRef(userId, exposureId)
            .get()
            .await()
            .toObject(Exposure::class.java)!!
    }

    override suspend fun delete(userId: String, exposureId: String) {
        exposureDocumentRef(userId, exposureId)
            .delete()
            .await()
    }

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val EXPOSURES_COLLECTION = "exposures"
        private const val CREATED_AT_FIELD = "createdAt"
        private const val REVIEW_FIELD = "review"
        private const val STATUS_FIELD = "status"
        private const val TITLE_FIELD = "title"
        private const val DESCRIPTION_FIELD = "description"
        private const val PREPARATION_FIELD = "preparation"
    }
}