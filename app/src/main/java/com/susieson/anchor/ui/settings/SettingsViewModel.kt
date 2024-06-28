package com.susieson.anchor.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.susieson.anchor.service.AuthService
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = SettingsViewModel.Factory::class)
class SettingsViewModel @AssistedInject constructor(
    @Assisted val userId: String,
    private val authService: AuthService
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(userId: String): SettingsViewModel
    }

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    val user = authService.user

    fun logout() {
        viewModelScope.launch {
            _error.value = null
            authService.signOut()
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            _error.value = null
            try {
                authService.deleteAccount()
            } catch (e: FirebaseException) {
                _error.value = e.localizedMessage
            }
        }
    }

    fun authenticate(email: String, password: String, action: () -> Unit) {
        viewModelScope.launch {
            _error.value = null
            try {
                authService.reAuthenticate(email, password)
                action()
            } catch (e: FirebaseException) {
                _error.value = e.localizedMessage
            }
        }
    }

    fun linkAccount(email: String, password: String) {
        viewModelScope.launch {
            _error.value = null
            try {
                authService.linkAccount(email, password)
            } catch (e: FirebaseException) {
                _error.value = e.localizedMessage
            }
        }
    }
}
