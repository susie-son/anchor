package com.susieson.anchor.ui.preparation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.model.Voyage
import com.susieson.anchor.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreparationViewModel @Inject constructor(
    private val storageService: StorageService
) : ViewModel() {
    fun add(voyage: Voyage) {
        viewModelScope.launch {
            storageService.add(voyage)
        }
    }
}