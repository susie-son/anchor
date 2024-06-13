package com.susieson.anchor.ui.preparation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.model.Preparation
import com.susieson.anchor.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreparationViewModel @Inject constructor(
    private val storageService: StorageService
) : ViewModel() {

    private val _exposureId = MutableStateFlow<String?>(null)
    val exposureId: StateFlow<String?> = _exposureId.asStateFlow()

    fun new(userId: String, title: String, description: String, preparation: Preparation) {
        viewModelScope.launch {
            val exposureId = storageService.addExposure(userId)
            _exposureId.value = exposureId
            storageService.updateExposure(userId, exposureId, title, description)
            storageService.updateExposure(userId, exposureId, preparation)
        }
    }
}