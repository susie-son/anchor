package com.susieson.anchor.ui.exposure.ready

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.model.Status
import com.susieson.anchor.service.NotificationService
import com.susieson.anchor.service.StorageService
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ExposureReadyViewModel.Factory::class)
class ExposureReadyViewModel @AssistedInject constructor(
    @Assisted private val userId: String,
    @Assisted private val exposure: Exposure,
    private val storageService: StorageService,
    private val notificationService: NotificationService
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(userId: String, exposure: Exposure): ExposureReadyViewModel
    }

    fun markAsInProgress() {
        viewModelScope.launch {
            storageService.updateExposure(userId, exposure.id, Status.IN_PROGRESS)
            notificationService.showReminderNotification(exposure.title, userId, exposure.id)
        }
    }
}
