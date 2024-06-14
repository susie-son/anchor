package com.susieson.anchor

import androidx.compose.runtime.mutableStateOf
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

    lateinit var userId: String
    val topAppBar = mutableStateOf(TopAppBarState.Default)
    var isInitialized = false

    init {
        viewModelScope.launch {
            userId = authService.getUserId()
            isInitialized = true
        }
    }

    fun setTopAppBar(state: TopAppBarState) {
        topAppBar.value = state
    }
}