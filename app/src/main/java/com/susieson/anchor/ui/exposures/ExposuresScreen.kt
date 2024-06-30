package com.susieson.anchor.ui.exposures

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.ui.components.LoadingItem

@Composable
fun ExposuresScreen(
    onNavigateSettings: (String) -> Unit,
    onNavigateExposure: (String, Exposure) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExposuresViewModel = hiltViewModel()
) {
    val exposures = viewModel.exposures.collectAsState(null).value

    Scaffold(
        topBar = { ExposuresTopBar { onNavigateSettings(viewModel.userId) } },
        floatingActionButton = {
            ExposuresFloatingActionButton { onNavigateExposure(viewModel.userId, Exposure()) }
        },
        modifier = modifier,
    ) { innerPadding ->
        when (exposures) {
            null -> LoadingItem(modifier = Modifier.fillMaxSize().padding(innerPadding))
            else -> ExposuresContent(
                exposures = exposures,
                onItemClick = { exposure -> onNavigateExposure(viewModel.userId, exposure) },
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            )
        }
    }
}
