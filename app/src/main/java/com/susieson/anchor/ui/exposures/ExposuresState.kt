package com.susieson.anchor.ui.exposures

import com.susieson.anchor.model.Exposure

sealed interface ExposuresState {
    data object Loading : ExposuresState
    data object Empty : ExposuresState
    data class Contained(val exposures: List<Exposure>) : ExposuresState
}

fun getExposuresState(exposures: List<Exposure>?): ExposuresState {
    return when {
        exposures == null -> ExposuresState.Loading
        exposures.isEmpty() -> ExposuresState.Empty
        else -> ExposuresState.Contained(exposures)
    }
}
