package com.susieson.anchor.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.susieson.anchor.service.AuthService
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _error = MutableSharedFlow<String?>()
    val error = _error.asSharedFlow()

    val user = authService.currentUser

    fun logout() {
        authService.signOut()
    }

    fun deleteAccount() {
        viewModelScope.launch {
            try {
                authService.deleteAccount()
            } catch (e: FirebaseException) {
                _error.emit(e.localizedMessage)
            }
        }
    }

    fun authenticate(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                authService.reauthenticateWithEmailAndPassword(email, password)
                onSuccess()
            } catch (e: FirebaseException) {
                _error.emit(e.localizedMessage)
            }
        }
    }

    fun linkAccount(email: String, password: String) {
        viewModelScope.launch {
            try {
                authService.linkAccountWithEmailAndPassword(email, password)
            } catch (e: FirebaseException) {
                _error.emit(e.localizedMessage)
            }
        }
    }
}
