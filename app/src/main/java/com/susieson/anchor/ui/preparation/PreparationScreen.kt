package com.susieson.anchor.ui.preparation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.susieson.anchor.R
import com.susieson.anchor.model.Preparation
import com.susieson.anchor.ui.components.DiscardDialog
import com.susieson.anchor.ui.components.TextFieldColumn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreparationTopBar(
    modifier: Modifier = Modifier,
    isValid: Boolean,
    isEmpty: Boolean,
    onBack: () -> Unit,
    onDiscard: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.preparation_top_bar_title)) },
        navigationIcon = {
            IconButton(onClick = { if (isEmpty) onBack() else onDiscard() }) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_close),
                    contentDescription = "Close"
                )
            }
        },
        actions = {
            IconButton(onClick = onConfirm, enabled = isValid) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_done),
                    contentDescription = "Done"
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun PreparationScreen(
    modifier: Modifier = Modifier,
    userId: String,
    onBack: () -> Unit = {},
    onNext: (String, String) -> Unit = { _, _ -> },
    preparationViewModel: PreparationViewModel = hiltViewModel()
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

    val isValid = !titleError && !descriptionError && !thoughtsError && !interpretationsError && !behaviorsError && !actionsError
    val isEmpty = title.isBlank() && description.isBlank() && thoughts.isEmpty() && interpretations.isEmpty() && behaviors.isEmpty() && actions.isEmpty()

    val exposureId by preparationViewModel.exposureId.collectAsState()

    LaunchedEffect(exposureId) {
        exposureId?.let {
            onNext(userId, it)
        }
    }

    if (openDiscardDialog) {
        DiscardDialog(
            onConfirm = onBack,
            onDismiss = {
                openDiscardDialog = false
            }
        )
    }

    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        PreparationTopBar(
            isValid = isValid,
            isEmpty = isEmpty,
            onBack = onBack,
            onDiscard = { openDiscardDialog = true },
            onConfirm = {
                val preparation = Preparation(
                    thoughts = thoughts,
                    interpretations = interpretations,
                    behaviors = behaviors,
                    actions = actions
                )
                preparationViewModel.new(userId, title, description, preparation)
            }
        )
    }) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
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
}