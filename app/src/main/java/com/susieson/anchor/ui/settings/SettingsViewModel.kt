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

    private val _actionComplete = MutableStateFlow(false)
    val actionComplete: StateFlow<Boolean> = _actionComplete

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

    fun reauthenticate(email: String, password: String, action: () -> Unit) {
        viewModelScope.launch {
            _error.value = null
            try {
                authService.reauthenticate(email, password)
                action()
                _actionComplete.value = true
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun onActionComplete() {
        _actionComplete.value = false
    }
}
