package com.susieson.anchor.ui.exposure.preparation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.susieson.anchor.R
import com.susieson.anchor.model.Preparation
import com.susieson.anchor.ui.components.form.DiscardConfirmationDialog
import com.susieson.anchor.ui.components.form.FormBackHandler
import com.susieson.anchor.ui.components.form.FormTopAppBar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PreparationForm(
    onTopBarChange: (@Composable () -> Unit) -> Unit,
    userId: String,
    exposureId: String,
    onDiscard: () -> Unit,
    addPreparation: (String, String, String, String, Preparation) -> Unit,
    modifier: Modifier = Modifier
) {
    val basicState = remember { mutableStateOf(BasicFormSectionState()) }
    val prepState = remember { mutableStateOf(PreparationFormSectionState()) }

    val basicListener = BaseBasicFormSectionListener(basicState)
    val prepListener = BasePreparationFormSectionListener(prepState)

    var showDiscardConfirmation by remember { mutableStateOf(false) }

    val isValid = basicState.value.isValid && prepState.value.isValid
    val isEmpty = basicState.value.isEmpty && prepState.value.isEmpty

    val title = basicState.value.title
    val description = basicState.value.description
    val thoughts = prepState.value.thoughts
    val interpretations = prepState.value.interpretations
    val behaviors = prepState.value.behaviors
    val actions = prepState.value.actions

    val preparation = Preparation(thoughts, interpretations, behaviors, actions)

    onTopBarChange {
        FormTopAppBar(
            title = { Text(stringResource(R.string.preparation_top_bar_title)) },
            isValid = isValid,
            isEmpty = isEmpty,
            onDiscard = onDiscard,
            onShowDiscardConfirmation = { showDiscardConfirmation = true },
            onActionClick = {
                addPreparation(userId, exposureId, title, description, preparation)
            }
        )
    }

    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState())
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
    DiscardConfirmationDialog(showDiscardConfirmation, onDiscard) { showDiscardConfirmation = it }
    FormBackHandler(isEmpty, onDiscard) { showDiscardConfirmation = true }
}
