package com.susieson.anchor.ui.exposure.preparation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.susieson.anchor.R
import com.susieson.anchor.ui.components.LabeledItemWithSupporting
import com.susieson.anchor.ui.components.Operation
import com.susieson.anchor.ui.components.TextFieldColumn

@Composable
fun ExposurePreparationContent(
    state: ExposurePreparationState,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onThoughtChange: (Operation, String) -> Unit,
    onInterpretationChange: (Operation, String) -> Unit,
    onBehaviorChange: (Operation, String) -> Unit,
    onActionChange: (Operation, String) -> Unit,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(innerPadding)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BasicSection(
            title = state.title,
            description = state.description,
            onTitleChange = onTitleChange,
            onDescriptionChange = onDescriptionChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        PreparationSection(
            thoughts = state.thoughts,
            interpretations = state.interpretations,
            behaviors = state.behaviors,
            actions = state.actions,
            onThoughtChange = onThoughtChange,
            onInterpretationChange = onInterpretationChange,
            onBehaviorChange = onBehaviorChange,
            onActionChange = onActionChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
    }
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
                if (title.isBlank()) {
                    Text(stringResource(R.string.preparation_title_error))
                }
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
                if (description.isBlank()) {
                    Text(stringResource(R.string.preparation_description_error))
                }
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
    onThoughtChange: (Operation, String) -> Unit,
    onInterpretationChange: (Operation, String) -> Unit,
    onBehaviorChange: (Operation, String) -> Unit,
    onActionChange: (Operation, String) -> Unit,
    modifier: Modifier = Modifier
) {
    data class PreparationCategory(
        @StringRes val label: Int,
        @StringRes val body: Int,
        val items: List<String>,
        val onChange: (Operation, String) -> Unit
    )
    val categories = listOf(
        PreparationCategory(
            R.string.preparation_thoughts_label,
            R.string.preparation_thoughts_body,
            thoughts,
            onThoughtChange
        ),
        PreparationCategory(
            R.string.preparation_interpretations_label,
            R.string.preparation_interpretations_body,
            interpretations,
            onInterpretationChange
        ),
        PreparationCategory(
            R.string.preparation_behaviors_label,
            R.string.preparation_behaviors_body,
            behaviors,
            onBehaviorChange
        ),
        PreparationCategory(
            R.string.preparation_actions_label,
            R.string.preparation_actions_body,
            actions,
            onActionChange
        )
    )
    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        categories.forEach { category ->
            LabeledItemWithSupporting(
                labelText = { Text(stringResource(category.label)) },
                supportingText = {
                    Text(
                        text = stringResource(category.body),
                        color = if (category.items.isEmpty()) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    )
                }
            ) {
                TextFieldColumn(
                    texts = category.items,
                    onAdd = { category.onChange(Operation.ADD, it) },
                    onRemove = { category.onChange(Operation.REMOVE, it) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExposurePreparationContentPreview() {
    ExposurePreparationContent(
        state = ExposurePreparationState(),
        onTitleChange = {},
        onDescriptionChange = {},
        onThoughtChange = { _, _ -> },
        onInterpretationChange = { _, _ -> },
        onBehaviorChange = { _, _ -> },
        onActionChange = { _, _ -> },
        innerPadding = PaddingValues()
    )
}
