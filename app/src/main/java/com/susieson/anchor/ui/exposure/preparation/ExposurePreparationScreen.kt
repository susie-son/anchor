package com.susieson.anchor.ui.exposure.preparation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.susieson.anchor.R
import com.susieson.anchor.ui.components.DiscardDialog
import com.susieson.anchor.ui.components.FormBackHandler
import com.susieson.anchor.ui.components.LabeledItemWithSupporting
import com.susieson.anchor.ui.components.TextFieldColumn
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExposurePreparationScreen(
    viewModel: ExposurePreparationViewModel,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val title = remember { viewModel.title }
    val description = remember { viewModel.description }
    val thoughts = remember { viewModel.thoughts }
    val interpretations = remember { viewModel.interpretations }
    val behaviors = remember { viewModel.behaviors }
    val actions = remember { viewModel.actions }

    val isEmpty = remember { viewModel.isEmpty }

    val showDiscardDialog = remember { viewModel.showDiscardDialog }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val bringIntoViewRequester = remember { BringIntoViewRequester() }

        val coroutineScope = rememberCoroutineScope()

        OutlinedTextField(
            value = title,
            isError = title.isBlank(),
            label = { Text(stringResource(R.string.preparation_title_label)) },
            placeholder = {},
            supportingText = {
                if (title.isBlank()) Text(
                    stringResource(R.string.preparation_title_error),
                    color = MaterialTheme.colorScheme.error
                )
            },
            onValueChange = viewModel::onTitleChange,
            modifier = Modifier
                .fillMaxWidth()
                .bringIntoViewRequester(bringIntoViewRequester)
                .onFocusChanged {
                    if (it.isFocused) {
                        coroutineScope.launch {
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                }
                .focusTarget(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        OutlinedTextField(
            value = description,
            isError = description.isBlank(),
            label = { Text(stringResource(R.string.preparation_description_label)) },
            placeholder = {},
            supportingText = {
                if (description.isBlank()) Text(
                    stringResource(R.string.preparation_description_error),
                    color = MaterialTheme.colorScheme.error
                )
            },
            onValueChange = viewModel::onDescriptionChange,
            modifier = Modifier
                .fillMaxWidth()
                .bringIntoViewRequester(bringIntoViewRequester)
                .onFocusChanged {
                    if (it.isFocused) {
                        coroutineScope.launch {
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                }
                .focusTarget(),
            singleLine = false,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        )
        LabeledItemWithSupporting(
            label = {
                Text(
                    stringResource(R.string.preparation_thoughts_label),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            supporting = {
                Text(
                    stringResource(R.string.preparation_thoughts_body),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (thoughts.isEmpty()) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            },
            content = {
                TextFieldColumn(
                    texts = thoughts,
                    bringIntoViewRequester = bringIntoViewRequester,
                    onAdd = viewModel::onThoughtAdded,
                    onDelete = viewModel::onThoughtRemoved
                )
            }
        )
        LabeledItemWithSupporting(
            label = {
                Text(
                    stringResource(R.string.preparation_interpretations_label),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            supporting = {
                Text(
                    stringResource(R.string.preparation_interpretations_body),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (interpretations.isEmpty()) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            },
            content = {
                TextFieldColumn(
                    texts = interpretations,
                    bringIntoViewRequester = bringIntoViewRequester,
                    onAdd = viewModel::onInterpretationAdded,
                    onDelete = viewModel::onInterpretationRemoved
                )
            }
        )
        LabeledItemWithSupporting(
            label = {
                Text(
                    stringResource(R.string.preparation_behaviors_label),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            supporting = {
                Text(
                    stringResource(R.string.preparation_behaviors_body),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (behaviors.isEmpty()) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            },
            content = {
                TextFieldColumn(
                    texts = behaviors,
                    bringIntoViewRequester = bringIntoViewRequester,
                    onAdd = viewModel::onBehaviorAdded,
                    onDelete = viewModel::onBehaviorRemoved
                )
            }
        )
        LabeledItemWithSupporting(
            label = {
                Text(
                    stringResource(R.string.preparation_actions_label),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            supporting = {
                Text(
                    stringResource(R.string.preparation_actions_body),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (actions.isEmpty()) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            },
            content = {
                TextFieldColumn(
                    texts = actions,
                    bringIntoViewRequester = bringIntoViewRequester,
                    onAdd = viewModel::onActionAdded,
                    onDelete = viewModel::onActionRemoved
                )
            }
        )
    }
    DiscardDialog(
        show = showDiscardDialog,
        onDiscard = {
            onNavigateUp()
            viewModel.deleteExposure()
        },
        onSetShow = viewModel::onShowDiscardDialogChange
    )
    FormBackHandler(
        isEmpty = isEmpty,
        onDiscard = {
            onNavigateUp()
            viewModel.deleteExposure()
        },
        onShowDiscardDialog = { viewModel.onShowDiscardDialogChange(true) }
    )
}
