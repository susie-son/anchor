package com.susieson.anchor.ui.ready

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.susieson.anchor.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadyTopBar(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.ready_top_bar_title)) },
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
fun ReadyScreen(
    modifier: Modifier = Modifier,
    userId: String,
    exposureId: String,
    onBack: () -> Unit = {},
    readyViewModel: ReadyViewModel = hiltViewModel()
) {
    var checked by rememberSaveable { mutableStateOf(listOf(false, false)) }

    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        ReadyTopBar(
            onBack = onBack,
        )
    }) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(stringResource(R.string.ready_description))
            Column(modifier = modifier.weight(1f)) {
                ListItem(headlineContent = { Text(stringResource(R.string.ready_check_1)) },
                    trailingContent = {
                        Checkbox(
                            checked = checked[0],
                            onCheckedChange = { checked = listOf(it, checked[1]) })
                    })
                ListItem(headlineContent = { Text(stringResource(R.string.ready_check_2)) },
                    trailingContent = {
                        Checkbox(
                            checked = checked[1],
                            onCheckedChange = { checked = listOf(checked[0], it) })
                    })
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier.fillMaxWidth()
            ) {
                OutlinedButton(onClick = onBack) { Text(stringResource(R.string.ready_dismiss_button)) }
                FilledTonalButton(
                    onClick = {
                        readyViewModel.update(userId, exposureId)
                        onBack()
                    },
                    enabled = checked.all { it }) { Text(stringResource(R.string.ready_confirm_button)) }
            }
        }
    }
}