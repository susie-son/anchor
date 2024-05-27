package com.susieson.anchor.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.model.User
import com.susieson.anchor.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    val user: Flow<User> = authService.currentUser

    fun login() {
        viewModelScope.launch {
            authService.createAnonymousAccount()
        }
    }
}