package com.susieson.anchor.ui

import com.susieson.anchor.ui.components.AnchorFabState
import com.susieson.anchor.ui.components.AnchorFormState
import com.susieson.anchor.ui.components.AnchorTopAppBarState

data class AnchorScreenState(
    val topAppBarState: AnchorTopAppBarState,
    val fabState: AnchorFabState? = null,
    val formState: AnchorFormState? = null,
) {
    companion object {
        val Default = AnchorScreenState(
            topAppBarState = AnchorTopAppBarState.Default,
        )
    }
}