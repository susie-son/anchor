package com.susieson.anchor.ui.exposures

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.service.AuthService
import com.susieson.anchor.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExposuresViewModel @Inject constructor(
    private val storageService: StorageService,
    private val authService: AuthService
) : ViewModel() {
    private val _exposures = MutableStateFlow<List<Exposure>?>(null)
    val exposures = _exposures.asStateFlow()

    fun get(userId: String) {
        viewModelScope.launch {
            storageService.getExposureList(userId).collect { exposures ->
                _exposures.value = exposures
            }
        }
    }
}