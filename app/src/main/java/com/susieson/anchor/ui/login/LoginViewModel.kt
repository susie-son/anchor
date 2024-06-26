package com.susieson.anchor.ui.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.susieson.anchor.model.User
import com.susieson.anchor.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authService: AuthService) : ViewModel() {

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val passwordVisible = mutableStateOf(false)

    init {
        viewModelScope.launch {
            authService.user.collect {
                _user.value = it
            }
        }
    }

    fun onEmailChange(email: String) {
        this.email.value = email
    }

    fun onPasswordChange(password: String) {
        this.password.value = password
    }

    fun onPasswordVisibleChange() {
        passwordVisible.value = !passwordVisible.value
    }

    fun createAnonymousAccount() {
        viewModelScope.launch {
            _error.value = null
            try {
                authService.createAnonymousAccount()
            } catch (e: FirebaseException) {
                _error.value = e.localizedMessage
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _error.value = null
            try {
                authService.authenticate(email, password)
            } catch (e: FirebaseException) {
                _error.value = e.localizedMessage
            }
        }
    }
}
