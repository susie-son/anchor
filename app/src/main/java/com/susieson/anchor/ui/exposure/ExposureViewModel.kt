package com.susieson.anchor.ui.exposure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.model.Preparation
import com.susieson.anchor.model.Review
import com.susieson.anchor.model.Status
import com.susieson.anchor.service.NotificationService
import com.susieson.anchor.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExposureViewModel @Inject constructor(
    private val storageService: StorageService,
    private val notificationService: NotificationService
) : ViewModel() {

    private val _exposure = MutableStateFlow<Exposure?>(null)
    val exposure = _exposure.asStateFlow()

    fun new(userId: String) {
        viewModelScope.launch {
            val exposureId = storageService.addExposure(userId)
            storageService.getExposure(userId, exposureId).collect { exposure ->
                _exposure.value = exposure
            }
        }
    }

    fun update(userId: String, exposureId: String, title: String) {
        viewModelScope.launch {
            storageService.updateExposure(userId, exposureId, Status.IN_PROGRESS)
            notificationService.showReminderNotification(title, userId, exposureId)
        }
    }

    fun add(userId: String, exposureId: String, review: Review) {
        viewModelScope.launch {
            storageService.updateExposure(userId, exposureId, review)
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
            storageService.updateExposure(userId, exposureId, title, description, preparation)
        }
    }

    fun get(userId: String, exposureId: String) {
        viewModelScope.launch {
            storageService.getExposure(userId, exposureId).collect { exposure ->
                _exposure.value = exposure
            }
        }
    }

    fun delete(userId: String, exposureId: String) {
        viewModelScope.launch {
            storageService.deleteExposure(userId, exposureId)
        }
    }
}