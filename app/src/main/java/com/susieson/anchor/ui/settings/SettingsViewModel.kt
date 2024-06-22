package com.susieson.anchor.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {
    val user = authService.user

    fun logout() {
        viewModelScope.launch {
            authService.signOut()
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            authService.deleteAccount()
        }
    }

    fun reauthenticate(email: String, password: String) {
        viewModelScope.launch {
            authService.reauthenticate(email, password)
        }
    }
}
