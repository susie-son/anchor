package com.susieson.anchor.ui.preparation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreparationViewModel @Inject constructor(
    private val storageService: StorageService
) : ViewModel() {
    fun add(exposure: Exposure) {
        viewModelScope.launch {
            storageService.add(exposure)
        }
    }
}