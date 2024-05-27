package com.susieson.anchor.ui.summary

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
class SummaryViewModel @Inject constructor(
    private val storageService: StorageService
) : ViewModel() {
    val exposure: LiveData<Exposure> get() = _exposure
    private val _exposure = MutableLiveData<Exposure>()

    fun get(userId: String, exposureId: String) {
        viewModelScope.launch {
            _exposure.value = storageService.get(userId, exposureId)
        }
    }
}