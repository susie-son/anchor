package com.susieson.anchor.ui.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SummaryViewModel @Inject constructor(
    private val storageService: StorageService
) : ViewModel() {

    private val _exposure = MutableStateFlow<Exposure?>(null)
    val exposure = _exposure.asStateFlow()

    fun get(userId: String, exposureId: String) {
        viewModelScope.launch {
            storageService.getExposure(userId, exposureId).collect { exposure ->
                _exposure.value = exposure
            }
        }
    }
}