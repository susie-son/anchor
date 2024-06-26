package com.susieson.anchor.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.susieson.anchor.Destination
import com.susieson.anchor.R
import com.susieson.anchor.ui.exposures.ExposuresViewModel

@Composable
fun FloatingActionButton(navController: NavHostController) {
    when (val route = currentRoute(navController)) {
        is Destination.Exposures -> {
            val viewModel: ExposuresViewModel = hiltViewModel(
                creationCallback = { factory: ExposuresViewModel.Factory ->
                    factory.create(route.userId)
                }
            )
            ExtendedFloatingActionButton(
                onClick = { viewModel.addExposure() },
                content = {
                    Icon(Icons.Default.Add, null)
                    Text(stringResource(R.string.exposures_start_button))
                }
            )
        }
        else -> {}
    }
}
