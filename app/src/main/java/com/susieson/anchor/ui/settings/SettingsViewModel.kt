package com.susieson.anchor.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

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

    fun reAuthenticate(email: String, password: String, action: () -> Unit) {
        viewModelScope.launch {
            _error.value = null
            try {
                authService.reAuthenticate(email, password)
                action()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun linkAccount(email: String, password: String) {
        viewModelScope.launch {
            _error.value = null
            try {
                authService.linkAccount(email, password)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
