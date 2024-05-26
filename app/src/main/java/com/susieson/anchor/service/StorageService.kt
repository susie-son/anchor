package com.susieson.anchor.service

import com.susieson.anchor.model.Voyage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface StorageService {
    val voyages: Flow<List<Voyage>>

    suspend fun getVoyage(voyageId: String)
    suspend fun save(voyage: Voyage)
    suspend fun update(voyage: Voyage)
    suspend fun delete(voyageId: String)
    suspend fun deleteAllForUser(userId: String)
}

class StorageServiceImpl @Inject constructor(): StorageService {
    override val voyages: Flow<List<Voyage>>
        get() = TODO()

    override suspend fun getVoyage(voyageId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun save(voyage: Voyage) {
        TODO("Not yet implemented")
    }

    override suspend fun update(voyage: Voyage) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(voyageId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllForUser(userId: String) {
        TODO("Not yet implemented")
    }
}