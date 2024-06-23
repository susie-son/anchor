package com.susieson.anchor.ui.settings

import com.susieson.anchor.model.AnchorUser

sealed interface UserSettingsState {
    data object Loading : UserSettingsState
    data class Anonymous(val error: String?) : UserSettingsState
    data class Authenticated(val email: String, val error: String?) : UserSettingsState
}

fun getUserSettingsState(user: AnchorUser?, error: String?): UserSettingsState {
    return when (user) {
        null -> UserSettingsState.Loading
        else -> {
            when (user.isAnonymous) {
                true -> UserSettingsState.Anonymous(error)
                false -> UserSettingsState.Authenticated(user.email!!, error)
            }
        }
    }
}
