package com.susieson.anchor.ui.exposure.summary

import androidx.lifecycle.ViewModel
import com.susieson.anchor.model.Exposure
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = ExposureSummaryViewModel.Factory::class)
class ExposureSummaryViewModel @AssistedInject constructor(
    @Assisted val exposure: Exposure
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(exposure: Exposure): ExposureSummaryViewModel
    }
}
