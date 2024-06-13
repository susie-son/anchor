package com.susieson.anchor.service

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.model.Preparation
import com.susieson.anchor.model.Review
import com.susieson.anchor.model.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface StorageService {
    suspend fun addExposure(userId: String): String
    suspend fun updateExposure(userId: String, exposureId: String, title: String, description: String)
    suspend fun updateExposure(userId: String, exposureId: String, preparation: Preparation)
    suspend fun updateExposure(userId: String, exposureId: String, review: Review)
    suspend fun updateExposure(userId: String, exposureId: String, status: Status)
    suspend fun getExposureList(userId: String): Flow<List<Exposure>>
    suspend fun getExposure(userId: String, exposureId: String): Flow<Exposure>
    suspend fun getExposureSync(userId: String, exposureId: String): Exposure
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

    override suspend fun addExposure(userId: String): String {
        val exposure = Exposure()
        val document = exposuresCollectionRef(userId)
            .add(exposure)
            .await()
        val exposureWithId = exposure.copy(id = document.id, updatedAt = Timestamp.now())
        document.set(exposureWithId)
        return document.id
    }

    override suspend fun updateExposure(
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
                Status.READY,
                UPDATED_AT_FIELD,
                Timestamp.now()
            )
            .await()
    }

    override suspend fun updateExposure(userId: String, exposureId: String, preparation: Preparation) {
        exposureDocumentRef(userId, exposureId)
            .update(
                PREPARATION_FIELD,
                preparation,
                UPDATED_AT_FIELD,
                Timestamp.now()
            )
            .await()
    }

    override suspend fun updateExposure(userId: String, exposureId: String, review: Review) {
        exposureDocumentRef(userId, exposureId)
            .update(
                REVIEW_FIELD,
                review,
                STATUS_FIELD,
                Status.COMPLETED,
                UPDATED_AT_FIELD,
                Timestamp.now()
            )
            .await()
    }

    override suspend fun updateExposure(userId: String, exposureId: String, status: Status) {
        exposureDocumentRef(userId, exposureId)
            .update(
                STATUS_FIELD,
                status,
                UPDATED_AT_FIELD,
                Timestamp.now()
            )
            .await()
    }

    override suspend fun getExposureList(userId: String): Flow<List<Exposure>> {
        return exposuresCollectionRef(userId)
            .orderBy(CREATED_AT_FIELD, Query.Direction.DESCENDING)
            .snapshots()
            .map { it.toObjects(Exposure::class.java) }
    }

    override suspend fun getExposure(userId: String, exposureId: String): Flow<Exposure> {
        return exposureDocumentRef(userId, exposureId)
            .snapshots()
            .map { it.toObject(Exposure::class.java)!! }
    }

    override suspend fun getExposureSync(userId: String, exposureId: String): Exposure {
        return exposureDocumentRef(userId, exposureId).get().await().toObject(Exposure::class.java)!!
    }

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val EXPOSURES_COLLECTION = "exposures"
        private const val CREATED_AT_FIELD = "createdAt"
        private const val UPDATED_AT_FIELD = "updatedAt"
        private const val REVIEW_FIELD = "review"
        private const val STATUS_FIELD = "status"
        private const val TITLE_FIELD = "title"
        private const val DESCRIPTION_FIELD = "description"
        private const val PREPARATION_FIELD = "preparation"
    }
}