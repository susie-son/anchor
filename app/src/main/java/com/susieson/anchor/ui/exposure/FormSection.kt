package com.susieson.anchor.ui.exposure

import androidx.compose.runtime.MutableState

interface FormSectionState {
    val isValid: Boolean
    val isEmpty: Boolean
}

abstract class FormSectionListener<T : FormSectionState>(val state: MutableState<T>) {
    fun updateState(update: T.() -> T) {
        state.value = state.value.update()
    }
}
