package com.susieson.anchor

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.service.AuthService
import com.susieson.anchor.ui.components.AnchorTopAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val authService: AuthService
) : ViewModel() {
    lateinit var userId: String
    val topAppBar = mutableStateOf(AnchorTopAppBarState.Default)
    var isInitialized = false

    init {
        viewModelScope.launch {
            userId = authService.getUserId()
            isInitialized = true
        }
    }

    fun setTopAppBar(state: AnchorTopAppBarState) {
        topAppBar.value = state
    }
}
