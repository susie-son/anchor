package com.susieson.anchor.ui.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.susieson.anchor.ui.components.LoadingScreen

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onStart: (String) -> Unit,
    splashViewModel: SplashViewModel = hiltViewModel()
) {
    val user by splashViewModel.user.collectAsState(null)

    LoadingScreen()

    LaunchedEffect(user) {
        val currentUser = user
        if (currentUser?.id != null && currentUser.id.isNotBlank()) {
            onStart(currentUser.id)
        } else {
            splashViewModel.login()
        }
    }
}