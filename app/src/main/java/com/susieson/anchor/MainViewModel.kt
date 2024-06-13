package com.susieson.anchor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authService: AuthService
): ViewModel() {

    var userId = ""

    init {
        viewModelScope.launch {
            authService.createAnonymousAccount()
            authService.currentUser.collect { user ->
                userId = user?.id ?: ""
            }
        }
    }
}