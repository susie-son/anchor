package com.susieson.anchor.ui.preparation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.model.Preparation
import com.susieson.anchor.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreparationViewModel @Inject constructor(
    private val storageService: StorageService
) : ViewModel() {

    private val _exposure = MutableStateFlow(Exposure())
    val exposure = _exposure.asStateFlow()

    fun get(
        userId: String,
        exposureId: String
    ) {
        viewModelScope.launch {
            storageService.get(userId, exposureId).collect { exposure ->
                _exposure.value = exposure
            }
        }
    }

    fun add(
        userId: String,
        exposureId: String,
        title: String,
        description: String,
        preparation: Preparation
    ) {
        viewModelScope.launch {
            storageService.add(userId, exposureId, title, description)
            storageService.add(userId, exposureId, preparation)
        }
    }

    fun new(userId: String, title: String, description: String, preparation: Preparation) {
        viewModelScope.launch {
            val exposureId = storageService.add(userId)
            storageService.add(userId, exposureId, title, description)
            storageService.add(userId, exposureId, preparation)
        }
    }
}