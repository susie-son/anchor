package com.susieson.anchor.ui.exposures

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.service.StorageService
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ExposuresViewModel.Factory::class)
class ExposuresViewModel @AssistedInject constructor(
    @Assisted val userId: String,
    private val storageService: StorageService
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(userId: String): ExposuresViewModel
    }

    private val _exposures = MutableStateFlow<List<Exposure>?>(null)
    val exposures: StateFlow<List<Exposure>?> = _exposures

    private val _exposure = MutableStateFlow<Exposure?>(null)
    val exposure: StateFlow<Exposure?> = _exposure

    init {
        viewModelScope.launch {
            storageService.getExposures(userId).collect {
                _exposures.value = it
            }
        }
    }

    fun addExposure() {
        viewModelScope.launch {
            _exposure.value = storageService.addExposure(userId)
        }
    }
}
