package com.susieson.anchor.ui.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onStart: (String) -> Unit,
    splashViewModel: SplashViewModel = hiltViewModel()
) {
    val user by splashViewModel.user.collectAsState(null)

    LaunchedEffect(user) {
        user?.let {
            if (it.id.isNotEmpty()) {
                onStart(it.id)
            }
        }
    }

    LaunchedEffect(true) {
        splashViewModel.login()
    }
}