package com.susieson.anchor.ui.exposure.preparation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.ui.components.DiscardDialog
import com.susieson.anchor.ui.components.FormBackHandler

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExposurePreparationScreen(
    userId: String,
    exposure: Exposure,
    viewModel: ExposurePreparationViewModel,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val basicState by remember { viewModel.basicState }
    val prepState by remember { viewModel.prepState }

    val basicListener by remember { viewModel.basicListener }
    val prepListener by remember { viewModel.prepListener }

    val showDiscardDialog by remember { viewModel.showDiscardDialog }

    val isEmpty by remember { viewModel.isEmpty }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val bringIntoViewRequester = remember { BringIntoViewRequester() }
        BasicFormSection(
            state = basicState,
            listener = basicListener,
            bringIntoViewRequester = bringIntoViewRequester,
            modifier = Modifier.fillMaxWidth()
        )
        PreparationFormSection(
            state = prepState,
            listener = prepListener,
            bringIntoViewRequester = bringIntoViewRequester,
            modifier = Modifier.fillMaxWidth()
        )
    }
    DiscardDialog(
        show = showDiscardDialog,
        onDiscard = {
            onNavigateUp()
            viewModel.deleteExposure(userId, exposure.id)
        },
        onSetShow = viewModel::setShowDiscardDialog
    )
    FormBackHandler(
        isEmpty = isEmpty,
        onDiscard = {
            onNavigateUp()
            viewModel.deleteExposure(userId, exposure.id)
        },
        onShowDiscardDialog = { viewModel.setShowDiscardDialog(true) }
    )
}
