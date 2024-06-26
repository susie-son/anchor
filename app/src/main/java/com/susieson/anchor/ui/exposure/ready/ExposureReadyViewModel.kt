package com.susieson.anchor.ui.exposure.ready

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    @Assisted("userId") private val userId: String,
    @Assisted("exposureId") private val exposureId: String,
    private val storageService: StorageService,
    private val notificationService: NotificationService
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("userId") userId: String, @Assisted("exposureId")exposureId: String): ExposureReadyViewModel
    }

    fun markAsInProgress(title: String) {
        viewModelScope.launch {
            storageService.updateExposure(userId, exposureId, Status.IN_PROGRESS)
            notificationService.showReminderNotification(title, userId, exposureId)
        }
    }
}
