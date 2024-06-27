package com.susieson.anchor.ui.exposure.preparation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.susieson.anchor.R
import com.susieson.anchor.ui.components.DiscardDialog
import com.susieson.anchor.ui.components.FormBackHandler
import com.susieson.anchor.ui.components.LabeledItemWithSupporting
import com.susieson.anchor.ui.components.TextFieldColumn

@OptIn(ExperimentalMaterial3Api::class)
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
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.preparation_top_bar_title)) },
                navigationIcon = {
                    IconButton(
                        {
                            if (isEmpty) {
                                navController.navigateUp()
                            } else {
                                viewModel.onShowDiscardDialogChange(true)
                            }
                        }
                    ) {
                        Icon(
                            Icons.Default.Close,
                            stringResource(R.string.content_description_close)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.addPreparation()
                            navController.navigateUp()
                        },
                        enabled = isValid
                    ) {
                        Icon(Icons.Default.Done, stringResource(R.string.content_description_done))
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth(),
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
                singleLine = false,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth(),
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
                        onAdd = viewModel::onActionAdded,
                        onDelete = viewModel::onActionRemoved
                    )
                }
            )
        }
    }
    DiscardDialog(
        show = showDiscardDialog,
        onDiscard = navController::navigateUp,
        onSetShow = viewModel::onShowDiscardDialogChange
    )
    FormBackHandler(
        isEmpty = isEmpty,
        onDiscard = navController::navigateUp,
        onShowDiscardDialog = { viewModel.onShowDiscardDialogChange(true) }
    )
}
