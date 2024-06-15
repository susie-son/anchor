package com.susieson.anchor.ui.exposures

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.service.AuthService
import com.susieson.anchor.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ExposuresViewModel
@Inject
constructor(
    private val authService: AuthService,
    private val storageService: StorageService
) : ViewModel() {
    private lateinit var userId: String

    val exposures = MutableStateFlow<List<Exposure>?>(null)
    val exposureId = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch {
            userId = authService.getUserId()
            storageService.getExposureList(userId).collect {
                exposures.value = it
            }
        }
    }

    fun addExposure() {
        viewModelScope.launch {
            exposureId.value = storageService.addExposure(userId)
        }
    }

    fun resetExposureId() {
        exposureId.value = null
    }
}
