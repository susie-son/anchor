package com.susieson.anchor.ui.exposure.preparation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.model.Preparation
import com.susieson.anchor.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExposurePreparationViewModel @Inject constructor(
    private val storageService: StorageService,
) : ViewModel() {
    fun addPreparation(
        userId: String,
        exposureId: String,
        title: String,
        description: String,
        preparation: Preparation,
    ) {
        viewModelScope.launch {
            storageService.updateExposure(userId, exposureId, title, description, preparation)
        }
    }

    fun deleteExposure(userId: String, exposureId: String) {
        viewModelScope.launch {
            storageService.deleteExposure(userId, exposureId)
        }
    }
}
