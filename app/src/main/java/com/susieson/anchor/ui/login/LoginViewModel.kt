package com.susieson.anchor.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(private val authService: AuthService) : ViewModel() {

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun createAnonymousAccount() {
        viewModelScope.launch {
            _error.value = null
            try {
                authService.createAnonymousAccount()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _error.value = null
            try {
                authService.authenticate(email, password)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun createAccount(email: String, password: String) {
        viewModelScope.launch {
            _error.value = null
            try {
                authService.createAccount(email, password)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
