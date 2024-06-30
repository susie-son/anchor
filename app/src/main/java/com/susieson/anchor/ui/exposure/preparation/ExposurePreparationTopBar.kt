package com.susieson.anchor.ui.exposure.preparation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.susieson.anchor.R
import com.susieson.anchor.ui.components.onClose
import com.susieson.anchor.ui.components.onDone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposurePreparationTopBar(
    isFormEmpty: Boolean,
    isFormValid: Boolean,
    onNavigateUp: () -> Unit,
    onAddPreparation: () -> Unit,
    onShowDiscardDialogChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.preparation_top_bar_title)) },
        navigationIcon = {
            IconButton({ onClose(isFormEmpty, onNavigateUp, onShowDiscardDialogChange) }) {
                Icon(Icons.Default.Close, stringResource(R.string.content_description_close))
            }
        },
        actions = {
            IconButton({ onDone(onAddPreparation, onNavigateUp) }, enabled = isFormValid) {
                Icon(Icons.Default.Done, stringResource(R.string.content_description_done))
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun ExposurePreparationTopBarPreview() {
    ExposurePreparationTopBar(
        isFormEmpty = true,
        isFormValid = false,
        onNavigateUp = {},
        onAddPreparation = {},
        onShowDiscardDialogChange = {}
    )
}
