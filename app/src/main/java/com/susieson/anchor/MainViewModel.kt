package com.susieson.anchor

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.service.AuthService
import com.susieson.anchor.ui.AnchorScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val authService: AuthService
) : ViewModel() {

    var isReady = false
    val screenState = mutableStateOf(AnchorScreenState.Default)
    val user = authService.user

    init {
        viewModelScope.launch {
            authService.createAnonymousAccount()
            isReady = true
        }
    }

    fun setScreenState(state: AnchorScreenState) {
        screenState.value = state
    }
}
