package com.susieson.anchor.ui.exposure.ready

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.model.Status
import com.susieson.anchor.service.NotificationService
import com.susieson.anchor.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExposureReadyViewModel @Inject constructor(
    private val storageService: StorageService,
    private val notificationService: NotificationService
) : ViewModel() {
    fun markAsInProgress(userId: String, exposureId: String, title: String) {
        viewModelScope.launch {
            storageService.updateExposure(userId, exposureId, Status.IN_PROGRESS)
            notificationService.showReminderNotification(title, userId, exposureId)
        }
    }
}
