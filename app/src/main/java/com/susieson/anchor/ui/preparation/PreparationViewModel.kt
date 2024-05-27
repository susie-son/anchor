package com.susieson.anchor.ui.preparation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.model.Preparation
import com.susieson.anchor.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreparationViewModel @Inject constructor(
    private val storageService: StorageService
) : ViewModel() {

    private val _exposure = MutableLiveData(Exposure())
    val exposure: LiveData<Exposure> = _exposure

    fun get(
        userId: String,
        exposureId: String
    ) {
        viewModelScope.launch {
            _exposure.value = storageService.get(userId, exposureId)
        }
    }

    fun add(
        userId: String,
        exposureId: String,
        title: String,
        description: String,
        preparation: Preparation
    ) {
        viewModelScope.launch {
            storageService.add(userId, exposureId, title, description)
            storageService.add(userId, exposureId, preparation)
        }
    }

    fun delete(userId: String, exposureId: String) {
        viewModelScope.launch {
            storageService.delete(userId, exposureId)
        }
    }
}