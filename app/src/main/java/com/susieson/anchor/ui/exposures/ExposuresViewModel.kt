package com.susieson.anchor.ui.exposures

import androidx.lifecycle.ViewModel
import com.susieson.anchor.service.StorageService
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = ExposuresViewModel.Factory::class)
class ExposuresViewModel @AssistedInject constructor(
    @Assisted val userId: String,
    private val storageService: StorageService
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(userId: String): ExposuresViewModel
    }

    val exposures = storageService.getExposures(userId)
}
