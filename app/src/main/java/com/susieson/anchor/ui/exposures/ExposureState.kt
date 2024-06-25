package com.susieson.anchor.ui.exposures

import com.susieson.anchor.model.Exposure
import com.susieson.anchor.model.Status

sealed interface ExposureState {
    data object Loading : ExposureState
    data class Draft(val exposureId: String) : ExposureState
    data class Ready(val title: String) : ExposureState
    data object InProgress : ExposureState
    data class Completed(val exposure: Exposure) : ExposureState
}

fun getExposureState(exposure: Exposure?): ExposureState {
    return when (exposure) {
        null -> ExposureState.Loading
        else -> {
            when (exposure.status) {
                Status.DRAFT -> ExposureState.Draft(exposure.id)
                Status.READY -> ExposureState.Ready(exposure.title)
                Status.IN_PROGRESS -> ExposureState.InProgress
                Status.COMPLETED -> ExposureState.Completed(exposure)
            }
        }
    }
}
