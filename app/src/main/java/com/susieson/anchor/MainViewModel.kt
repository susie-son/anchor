package com.susieson.anchor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.susieson.anchor.model.AnchorUser
import com.susieson.anchor.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val authService: AuthService
) : ViewModel() {

    var isReady = false

    private val _user = MutableStateFlow<AnchorUser?>(null)
    val user: StateFlow<AnchorUser?> = _user

    init {
        viewModelScope.launch {
            authService.user.collect {
                _user.value = it
                isReady = true
            }
        }
    }
}
