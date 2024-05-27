package com.susieson.anchor.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.service.AuthService
import com.susieson.anchor.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authService: AuthService,
    private val storageService: StorageService
) : ViewModel() {

    private val _exposures = MutableLiveData<List<Exposure>>()
    val exposures: LiveData<List<Exposure>> = _exposures

    fun get() {
        viewModelScope.launch {
            _exposures.value = storageService.get()
        }
    }

    fun login() {
        viewModelScope.launch {
            authService.createAnonymousAccount()
        }
    }
}