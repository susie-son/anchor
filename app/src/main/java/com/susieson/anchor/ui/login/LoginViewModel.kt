package com.susieson.anchor.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.susieson.anchor.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authService: AuthService) : ViewModel() {

    private val _error = MutableSharedFlow<String?>()
    val error = _error.asSharedFlow()

    val user = authService.currentUser

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun onEmailChange(email: String) {
        _state.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _state.update { it.copy(password = password) }
    }

    fun onPasswordVisibleChange(passwordVisible: Boolean) {
        _state.update { it.copy(passwordVisible = passwordVisible) }
    }

    fun createAnonymousAccount() {
        viewModelScope.launch {
            try {
                authService.createAnonymousAccount()
            } catch (e: FirebaseException) {
                _error.emit(e.localizedMessage)
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            try {
                with(state.value) {
                    authService.signInWithEmailAndPassword(email, password)
                }
            } catch (e: FirebaseException) {
                _error.emit(e.localizedMessage)
            }
        }
    }
}

data class LoginState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false
)
