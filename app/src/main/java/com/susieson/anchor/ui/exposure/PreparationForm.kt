package com.susieson.anchor.ui.exposure

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import com.susieson.anchor.R
import com.susieson.anchor.ui.components.AnchorFormState
import com.susieson.anchor.ui.components.AnchorTopAppBarState
import com.susieson.anchor.ui.components.DiscardDialog
import com.susieson.anchor.ui.components.FormSection
import com.susieson.anchor.ui.components.FormTextField
import com.susieson.anchor.ui.components.LabeledFormTextFieldColumn

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
    setTopAppBar: (AnchorTopAppBarState) -> Unit,
    modifier: Modifier = Modifier
) {
    var openDiscardDialog by remember { mutableStateOf(false) }

    val isValid =
        title.isNotBlank() && description.isNotBlank() && thoughts.isNotEmpty() &&
            interpretations.isNotEmpty() && behaviors.isNotEmpty() && actions.isNotEmpty()
    val isEmpty =
        title.isBlank() && description.isBlank() && thoughts.isEmpty() &&
            interpretations.isEmpty() && behaviors.isEmpty() && actions.isEmpty()

    setTopAppBar(
        AnchorTopAppBarState(
            title = R.string.preparation_top_bar_title,
            onBack = onBack,
            formState =
            AnchorFormState(
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
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        BasicFormSection(
            title = title,
            description = description,
            onTitleChange = setTitle,
            onDescriptionChange = setDescription,
            modifier = modifier
        )
        PreparationFormSection(
            thoughts = thoughts,
            interpretations = interpretations,
            behaviors = behaviors,
            actions = actions,
            addThought = addThought,
            addInterpretation = addInterpretation,
            addBehavior = addBehavior,
            addAction = addAction,
            removeThought = removeThought,
            removeInterpretation = removeInterpretation,
            removeBehavior = removeBehavior,
            removeAction = removeAction,
            modifier = modifier
        )
    }
}

@Composable
private fun BasicFormSection(
    title: String,
    description: String,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    FormSection(
        modifier = modifier,
        {
            FormTextField(
                value = title,
                label = R.string.preparation_title_label,
                errorLabel = R.string.preparation_title_error,
                isError = title.isBlank(),
                imeAction = ImeAction.Next,
                onValueChange = onTitleChange,
                modifier = modifier
            )
        },
        {
            FormTextField(
                value = description,
                label = R.string.preparation_description_label,
                errorLabel = R.string.preparation_description_error,
                isError = description.isBlank(),
                imeAction = ImeAction.Done,
                onValueChange = onDescriptionChange,
                singleLine = false,
                modifier = modifier
            )
        }
    )
}

@Composable
private fun PreparationFormSection(
    thoughts: List<String>,
    interpretations: List<String>,
    behaviors: List<String>,
    actions: List<String>,
    addThought: (String) -> Unit,
    addInterpretation: (String) -> Unit,
    addBehavior: (String) -> Unit,
    addAction: (String) -> Unit,
    removeThought: (String) -> Unit,
    removeInterpretation: (String) -> Unit,
    removeBehavior: (String) -> Unit,
    removeAction: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    FormSection(
        modifier = modifier,
        {
            LabeledFormTextFieldColumn(
                texts = thoughts,
                label = R.string.preparation_thoughts_label,
                descriptionLabel = R.string.preparation_thoughts_body,
                onAdd = addThought,
                onDelete = removeThought,
                modifier = modifier
            )
            LabeledFormTextFieldColumn(
                texts = interpretations,
                label = R.string.preparation_interpretations_label,
                descriptionLabel = R.string.preparation_interpretations_body,
                onAdd = addInterpretation,
                onDelete = removeInterpretation,
                modifier = modifier
            )
        },
        {
            LabeledFormTextFieldColumn(
                texts = behaviors,
                label = R.string.preparation_behaviors_label,
                descriptionLabel = R.string.preparation_behaviors_body,
                onAdd = addBehavior,
                onDelete = removeBehavior,
                modifier = modifier
            )
        },
        {
            LabeledFormTextFieldColumn(
                texts = actions,
                label = R.string.preparation_actions_label,
                descriptionLabel = R.string.preparation_actions_body,
                onAdd = addAction,
                onDelete = removeAction,
                modifier = modifier
            )
        }
    )
}
