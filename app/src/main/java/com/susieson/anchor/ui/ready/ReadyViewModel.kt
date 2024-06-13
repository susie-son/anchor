package com.susieson.anchor.ui.ready

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.model.Status
import com.susieson.anchor.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadyViewModel @Inject constructor(
    private val storageService: StorageService
) : ViewModel() {
    fun update(userId: String, exposureId: String) {
        viewModelScope.launch {
            storageService.updateExposure(userId, exposureId, Status.IN_PROGRESS)
        }
    }
}