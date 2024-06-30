package com.susieson.anchor.ui.exposure.preparation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.susieson.anchor.ui.components.FormCloseHandler

@Composable
fun ExposurePreparationScreen(
    viewModel: ExposurePreparationViewModel,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            ExposurePreparationTopBar(
                isFormEmpty = state.isEmpty,
                isFormValid = state.isValid,
                onNavigateUp = onNavigateUp,
                onAddPreparation = viewModel::addPreparation,
                onShowDiscardDialogChange = viewModel::onShowDiscardDialogChanged
            )
        },
        modifier = modifier
    ) { innerPadding ->
        ExposurePreparationContent(
            state = state,
            onTitleChange = viewModel::onTitleChanged,
            onDescriptionChange = viewModel::onDescriptionChanged,
            onThoughtChange = viewModel::onThoughtChanged,
            onInterpretationChange = viewModel::onInterpretationChanged,
            onBehaviorChange = viewModel::onBehaviorChanged,
            onActionChange = viewModel::onActionChanged,
            innerPadding = innerPadding
        )
    }
    FormCloseHandler(
        isFormEmpty = state.isEmpty,
        showDiscardDialog = state.showDiscardDialog,
        onDiscard = onNavigateUp,
        onShowDialog = viewModel::onShowDiscardDialogChanged
    )
}
