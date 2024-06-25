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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.model.Preparation
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
    val basicState = remember { mutableStateOf(BasicFormSectionState()) }
    val prepState = remember { mutableStateOf(PreparationFormSectionState()) }

    val basicListener = BaseBasicFormSectionListener(basicState)
    val prepListener = BasePreparationFormSectionListener(prepState)

    var showDiscardDialog by remember { mutableStateOf(false) }

    val isValid = basicState.value.isValid && prepState.value.isValid
    val isEmpty = basicState.value.isEmpty && prepState.value.isEmpty

    val title = basicState.value.title
    val description = basicState.value.description
    val thoughts = prepState.value.thoughts
    val interpretations = prepState.value.interpretations
    val behaviors = prepState.value.behaviors
    val actions = prepState.value.actions

    val preparation = Preparation(thoughts, interpretations, behaviors, actions)

//    onTopBarChange {
//        CenterAlignedTopAppBar(
//            title = { Text(stringResource(R.string.preparation_top_bar_title)) },
//            navigationIcon = {
//                IconButton({
//                    if (isEmpty) onDiscard() else {
//                        showDiscardDialog = true
//                    }
//                }) {
//                    Icon(Icons.Default.Close, stringResource(R.string.content_description_close))
//                }
//            },
//            actions = {
//                IconButton(onClick = {
//                    addPreparation(userId, exposureId, title, description, preparation)
//                }, enabled = isValid) {
//                    Icon(Icons.Default.Done, stringResource(R.string.content_description_done))
//                }
//            },
//            modifier = modifier,
//        )
//    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val bringIntoViewRequester = remember { BringIntoViewRequester() }
        BasicFormSection(
            state = basicState.value,
            listener = basicListener,
            bringIntoViewRequester = bringIntoViewRequester,
            modifier = Modifier.fillMaxWidth()
        )
        PreparationFormSection(
            state = prepState.value,
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
        onSetShow = { showDiscardDialog = it }
    )
    FormBackHandler(
        isEmpty = isEmpty,
        onDiscard = {
            onNavigateUp()
            viewModel.deleteExposure(userId, exposure.id)
        },
        onShowDiscardDialog = { showDiscardDialog = true }
    )
}
