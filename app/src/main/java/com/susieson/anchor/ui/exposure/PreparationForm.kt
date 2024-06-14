package com.susieson.anchor.ui.exposure

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.susieson.anchor.FormState
import com.susieson.anchor.R
import com.susieson.anchor.TopAppBarState
import com.susieson.anchor.ui.components.DiscardDialog
import com.susieson.anchor.ui.components.TextFieldColumn

@Composable
fun PreparationForm(
    title: String,
    description: String,
    thoughts: List<String>,
    interpretations: List<String>,
    behaviors: List<String>,
    actions: List<String>,
    setTitle: (String) -> Unit,
    setDescription: (String) -> Unit,
    addThought: (String) -> Unit,
    addInterpretation: (String) -> Unit,
    addBehavior: (String) -> Unit,
    addAction: (String) -> Unit,
    removeThought: (String) -> Unit,
    removeInterpretation: (String) -> Unit,
    removeBehavior: (String) -> Unit,
    removeAction: (String) -> Unit,
    onDiscard: () -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit,
    setTopAppBarState: (TopAppBarState) -> Unit,
    modifier: Modifier = Modifier,
) {
    var openDiscardDialog by remember { mutableStateOf(false) }

    val titleError = title.isBlank()
    val descriptionError = description.isBlank()
    val thoughtsError = thoughts.isEmpty()
    val interpretationsError = interpretations.isEmpty()
    val behaviorsError = behaviors.isEmpty()
    val actionsError = actions.isEmpty()

    val isValid =
        !titleError && !descriptionError && !thoughtsError && !interpretationsError && !behaviorsError && !actionsError
    val isEmpty =
        title.isBlank() && description.isBlank() && thoughts.isEmpty() && interpretations.isEmpty() && behaviors.isEmpty() && actions.isEmpty()

    setTopAppBarState(
        TopAppBarState(
            title = R.string.preparation_top_bar_title,
            onBack = onBack,
            formState = FormState(
                isValid = isValid,
                isEmpty = isEmpty,
                onDiscard = { openDiscardDialog = true },
                onConfirm = onNext
            )
        )
    )

    if (openDiscardDialog) {
        DiscardDialog(
            onConfirm = {
                onDiscard()
                openDiscardDialog = false
                onBack()
            },
            onDismiss = {
                openDiscardDialog = false
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        OutlinedTextField(value = title,
            label = { Text(stringResource(R.string.preparation_title_label)) },
            onValueChange = setTitle,
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(),
            isError = titleError,
            supportingText = { if (titleError) Text(stringResource(R.string.preparation_title_error)) }
        )
        OutlinedTextField(
            value = description,
            label = { Text(stringResource(R.string.preparation_description_label)) },
            onValueChange = setDescription,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            isError = descriptionError,
            supportingText = { if (descriptionError) Text(stringResource(R.string.preparation_description_error)) }
        )
        Text(
            text = stringResource(R.string.preparation_thoughts_label),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(top = 24.dp)
        )
        Text(
            text = stringResource(R.string.preparation_thoughts_body),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
            color = if (thoughtsError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        )
        TextFieldColumn(thoughts, addThought, removeThought)
        Text(
            text = stringResource(R.string.preparation_interpretations_label),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(top = 24.dp)
        )
        Text(
            text = stringResource(R.string.preparation_interpretations_body),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
            color = if (interpretationsError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        )
        TextFieldColumn(interpretations, addInterpretation, removeInterpretation)
        Text(
            text = stringResource(R.string.preparation_behaviors_label),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(top = 24.dp)
        )
        Text(
            text = stringResource(R.string.preparation_behaviors_body),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
            color = if (behaviorsError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        )
        TextFieldColumn(behaviors, addBehavior, removeBehavior)
        Text(
            text = stringResource(R.string.preparation_actions_label),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(top = 24.dp)
        )
        Text(
            text = stringResource(R.string.preparation_actions_body),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
            color = if (actionsError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        )
        TextFieldColumn(actions, addAction, removeAction)
    }
}