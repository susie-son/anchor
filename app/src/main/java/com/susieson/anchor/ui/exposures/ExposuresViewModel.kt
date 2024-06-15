package com.susieson.anchor.ui.exposures

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ExposuresViewModel
@Inject
constructor(
    private val storageService: StorageService
) : ViewModel() {

    val exposures = MutableStateFlow<List<Exposure>?>(null)
    var exposureId by mutableStateOf<String?>(null)

    fun addExposure(userId: String) {
        viewModelScope.launch {
            exposureId = storageService.addExposure(userId)
        }
    }

    fun resetExposureId() {
        exposureId = null
    }

    fun getExposureList(userId: String) {
        viewModelScope.launch {
            storageService.getExposureList(userId).collect {
                exposures.value = it
            }
        }
    }
}
