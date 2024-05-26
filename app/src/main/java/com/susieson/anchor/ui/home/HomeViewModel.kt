package com.susieson.anchor.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.service.AuthService
import com.susieson.anchor.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authService: AuthService,
    storageService: StorageService
) : ViewModel() {
    val voyages = storageService.voyages

    fun login() {
        viewModelScope.launch {
            authService.createAnonymousAccount()
        }
    }
}