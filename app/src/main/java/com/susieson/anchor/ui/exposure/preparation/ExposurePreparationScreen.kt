package com.susieson.anchor.ui.exposure.preparation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.susieson.anchor.R
import com.susieson.anchor.ui.components.ErrorBodyText
import com.susieson.anchor.ui.components.ErrorText
import com.susieson.anchor.ui.components.FormDiscardHandler
import com.susieson.anchor.ui.components.LabelText
import com.susieson.anchor.ui.components.LabeledItemWithSupporting
import com.susieson.anchor.ui.components.TextFieldColumn
import com.susieson.anchor.ui.components.onClose
import com.susieson.anchor.ui.components.onDone

@Composable
fun ExposurePreparationScreen(
    viewModel: ExposurePreparationViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val title by remember { viewModel.title }
    val description by remember { viewModel.description }
    val thoughts = remember { viewModel.thoughts }
    val interpretations = remember { viewModel.interpretations }
    val behaviors = remember { viewModel.behaviors }
    val actions = remember { viewModel.actions }

    val isValid by remember { viewModel.isValid }
    val isEmpty by remember { viewModel.isEmpty }

    val showDiscardDialog by remember { viewModel.showDiscardDialog }

    Scaffold(
        topBar = {
            ExposurePreparationTopBar(
                onClose = {
                    onClose(isEmpty, navController::navigateUp, viewModel::onShowDiscardDialogChange)
                },
                onComplete = {
                    onDone(viewModel::addPreparation, navController::navigateUp)
                },
                isValid = isValid
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BasicSection(
                title = title,
                description = description,
                onTitleChange = viewModel::onTitleChange,
                onDescriptionChange = viewModel::onDescriptionChange,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
            PreparationSection(
                thoughts = thoughts,
                interpretations = interpretations,
                behaviors = behaviors,
                actions = actions,
                onThoughtAdd = viewModel::onThoughtAdded,
                onThoughtRemove = viewModel::onThoughtRemoved,
                onInterpretationAdd = viewModel::onInterpretationAdded,
                onInterpretationRemove = viewModel::onInterpretationRemoved,
                onBehaviorAdd = viewModel::onBehaviorAdded,
                onBehaviorRemove = viewModel::onBehaviorRemoved,
                onActionAdd = viewModel::onActionAdded,
                onActionRemove = viewModel::onActionRemoved,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
        }
    }
    FormDiscardHandler(
        isEmpty = isEmpty,
        showDiscardDialog = showDiscardDialog,
        onDiscard = navController::navigateUp,
        onShowDiscardDialog = viewModel::onShowDiscardDialogChange
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExposurePreparationTopBar(
    onClose: () -> Unit,
    onComplete: () -> Unit,
    isValid: Boolean,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.preparation_top_bar_title)) },
        navigationIcon = {
            IconButton(onClose) {
                Icon(Icons.Default.Close, stringResource(R.string.content_description_close))
            }
        },
        actions = {
            IconButton(onComplete, enabled = isValid) {
                Icon(Icons.Default.Done, stringResource(R.string.content_description_done))
            }
        },
        modifier = modifier
    )
}

@Composable
private fun BasicSection(
    title: String,
    description: String,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        OutlinedTextField(
            value = title,
            isError = title.isBlank(),
            label = { Text(stringResource(R.string.preparation_title_label)) },
            supportingText = {
                ErrorText(stringResource(R.string.preparation_title_error), title.isBlank())
            },
            onValueChange = onTitleChange,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = description,
            isError = description.isBlank(),
            label = { Text(stringResource(R.string.preparation_description_label)) },
            supportingText = {
                ErrorText(stringResource(R.string.preparation_description_error), description.isBlank())
            },
            onValueChange = onDescriptionChange,
            singleLine = false,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun PreparationSection(
    thoughts: List<String>,
    interpretations: List<String>,
    behaviors: List<String>,
    actions: List<String>,
    onThoughtAdd: (String) -> Unit,
    onThoughtRemove: (String) -> Unit,
    onInterpretationAdd: (String) -> Unit,
    onInterpretationRemove: (String) -> Unit,
    onBehaviorAdd: (String) -> Unit,
    onBehaviorRemove: (String) -> Unit,
    onActionAdd: (String) -> Unit,
    onActionRemove: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    data class PreparationCategory(
        @StringRes val label: Int,
        @StringRes val body: Int,
        val items: List<String>,
        val onAdd: (String) -> Unit,
        val onRemove: (String) -> Unit
    )
    val categories = listOf(
        PreparationCategory(
            R.string.preparation_thoughts_label,
            R.string.preparation_thoughts_body,
            thoughts,
            onThoughtAdd,
            onThoughtRemove
        ),
        PreparationCategory(
            R.string.preparation_interpretations_label,
            R.string.preparation_interpretations_body,
            interpretations,
            onInterpretationAdd,
            onInterpretationRemove
        ),
        PreparationCategory(
            R.string.preparation_behaviors_label,
            R.string.preparation_behaviors_body,
            behaviors,
            onBehaviorAdd,
            onBehaviorRemove
        ),
        PreparationCategory(
            R.string.preparation_actions_label,
            R.string.preparation_actions_body,
            actions,
            onActionAdd,
            onActionRemove
        )
    )
    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        categories.forEach { category ->
            LabeledItemWithSupporting(
                label = { LabelText(stringResource(category.label)) },
                supporting = {
                    ErrorBodyText(stringResource(category.body), category.items.isEmpty())
                }
            ) {
                TextFieldColumn(category.items, category.onAdd, category.onRemove)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BasicSectionPreview() {
    BasicSection(
        title = "Title",
        description = "Description",
        onTitleChange = {},
        onDescriptionChange = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun PreparationSectionPreview() {
    PreparationSection(
        thoughts = listOf("1", "2", "3"),
        interpretations = listOf("1", "2", "3"),
        behaviors = listOf("1", "2", "3"),
        actions = listOf("1", "2", "3"),
        onThoughtAdd = {},
        onThoughtRemove = {},
        onInterpretationAdd = {},
        onInterpretationRemove = {},
        onBehaviorAdd = {},
        onBehaviorRemove = {},
        onActionAdd = {},
        onActionRemove = {}
    )
}
