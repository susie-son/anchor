package com.susieson.anchor.ui.exposures

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExposuresViewModel
@Inject
constructor(
    private val storageService: StorageService
) : ViewModel() {

    private val _exposures = MutableStateFlow<List<Exposure>?>(null)
    private var _exposureId = MutableStateFlow<String?>(null)

    val exposures: StateFlow<List<Exposure>?> = _exposures
    val exposureId: StateFlow<String?> = _exposureId

    fun addExposure(userId: String) {
        viewModelScope.launch {
            _exposureId.value = storageService.addExposure(userId)
        }
    }

    fun resetExposureId() {
        _exposureId.value = null
    }

    fun getExposureList(userId: String) {
        viewModelScope.launch {
            storageService.getExposureList(userId).collect {
                _exposures.value = it
            }
        }
    }
}
