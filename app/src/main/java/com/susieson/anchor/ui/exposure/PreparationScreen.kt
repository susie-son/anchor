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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.susieson.anchor.FormState
import com.susieson.anchor.R
import com.susieson.anchor.TopAppBarState
import com.susieson.anchor.model.Preparation
import com.susieson.anchor.ui.components.DiscardDialog
import com.susieson.anchor.ui.components.TextFieldColumn

@Composable
fun PreparationScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onNext: (String, String, Preparation) -> Unit,
    onTopAppBarStateChanged: (TopAppBarState) -> Unit
) {

    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    var thoughts by rememberSaveable { mutableStateOf(listOf<String>()) }
    var interpretations by rememberSaveable { mutableStateOf(listOf<String>()) }
    var behaviors by rememberSaveable { mutableStateOf(listOf<String>()) }
    var actions by rememberSaveable { mutableStateOf(listOf<String>()) }

    var openDiscardDialog by rememberSaveable { mutableStateOf(false) }

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

    onTopAppBarStateChanged(
        TopAppBarState(
            title = R.string.preparation_top_bar_title,
            onBack = onBack,
            formState = FormState(
                isValid = isValid,
                isEmpty = isEmpty,
                onDiscard = { openDiscardDialog = true },
                onConfirm = {
                    val preparation = Preparation(
                        thoughts = thoughts,
                        interpretations = interpretations,
                        behaviors = behaviors,
                        actions = actions
                    )
                    onNext(title, description, preparation)
                }
            )
        )
    )

    if (openDiscardDialog) {
        DiscardDialog(
            onConfirm = {
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
            onValueChange = { title = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(),
            isError = titleError,
            supportingText = { if (titleError) Text(stringResource(R.string.preparation_title_error)) }
        )
        OutlinedTextField(
            value = description,
            label = { Text(stringResource(R.string.preparation_description_label)) },
            onValueChange = { description = it },
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
        TextFieldColumn(texts = thoughts,
            onAdd = { field -> thoughts = listOf(field) + thoughts },
            onDelete = { text -> thoughts = thoughts.filter { it != text } })
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
        TextFieldColumn(texts = interpretations,
            onAdd = { field -> interpretations = listOf(field) + interpretations },
            onDelete = { text -> interpretations = interpretations.filter { it != text } })
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
        TextFieldColumn(texts = behaviors,
            onAdd = { field -> behaviors = listOf(field) + behaviors },
            onDelete = { text -> behaviors = behaviors.filter { it != text } })
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
        TextFieldColumn(texts = actions,
            onAdd = { field -> actions = listOf(field) + actions },
            onDelete = { text -> actions = actions.filter { it != text } })
    }
}