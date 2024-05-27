package com.susieson.anchor.ui.summary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.susieson.anchor.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryTopBar(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.summary_top_bar_title)) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_close),
                    contentDescription = "Close"
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun SummaryScreen(
    modifier: Modifier = Modifier,
    exposureId: String,
    onBack: () -> Unit = {},
    summaryViewModel: SummaryViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { SummaryTopBar(onBack = onBack) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            summaryViewModel.exposure.observeAsState().value ?.let { exposure ->
                Text(exposure.title)
            }
            LaunchedEffect(exposureId) {
                summaryViewModel.get(exposureId)
            }
        }
    }
}