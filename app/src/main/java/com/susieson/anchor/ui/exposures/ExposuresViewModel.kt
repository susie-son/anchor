package com.susieson.anchor.ui.exposures

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExposuresViewModel @Inject constructor(
    private val storageService: StorageService
) : ViewModel() {
    private val _exposures = MutableLiveData<List<Exposure>>()
    val exposures: LiveData<List<Exposure>> = _exposures

    private val _exposureId = MutableLiveData<String?>()
    val exposureId: LiveData<String?> = _exposureId

    fun get(userId: String) {
        viewModelScope.launch {
            _exposures.value = storageService.get(userId)
        }
    }

    fun new(userId: String) {
        viewModelScope.launch {
            _exposureId.value = storageService.add(userId)
        }
    }

    fun reset() {
        viewModelScope.launch {
            _exposureId.value = null
        }
    }
}