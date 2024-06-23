package com.susieson.anchor.ui.exposure

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import com.susieson.anchor.R
import com.susieson.anchor.model.Preparation
import com.susieson.anchor.ui.components.AnchorScaffold
import com.susieson.anchor.ui.components.form.DiscardConfirmationDialog
import com.susieson.anchor.ui.components.form.FormBackHandler
import com.susieson.anchor.ui.components.form.FormSection
import com.susieson.anchor.ui.components.form.FormTextField
import com.susieson.anchor.ui.components.form.LabeledFormTextFieldColumn
import com.susieson.anchor.ui.components.form.formTopAppBar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PreparationForm(
    userId: String,
    exposureId: String,
    onDiscard: () -> Unit,
    addPreparation: (String, String, String, String, Preparation) -> Unit,
    setScaffold: (AnchorScaffold) -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val thoughts = remember { mutableStateListOf<String>() }
    val interpretations = remember { mutableStateListOf<String>() }
    val behaviors = remember { mutableStateListOf<String>() }
    val actions = remember { mutableStateListOf<String>() }

    var showDiscardConfirmation by remember { mutableStateOf(false) }

    val isValid = title.isNotBlank() && description.isNotBlank() && thoughts.isNotEmpty() &&
        interpretations.isNotEmpty() && behaviors.isNotEmpty() && actions.isNotEmpty()
    val isEmpty = title.isBlank() && description.isBlank() && thoughts.isEmpty() &&
        interpretations.isEmpty() && behaviors.isEmpty() && actions.isEmpty()

    setScaffold(
        AnchorScaffold(
            topAppBar = formTopAppBar(
                title = R.string.preparation_top_bar_title,
                isValid = isValid,
                isEmpty = isEmpty,
                onDiscard = onDiscard,
                onShowDiscardConfirmation = { showDiscardConfirmation = true },
                onActionClick = {
                    val preparation =
                        Preparation(thoughts, interpretations, behaviors, actions)
                    addPreparation(userId, exposureId, title, description, preparation)
                }
            )
        )
    )

    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        val bringIntoViewRequester = remember { BringIntoViewRequester() }
        val state = PreparationFormState()
        val listener = BasePreparationFormListener(state)

        BasicFormSection(
            title = title,
            description = description,
            onTitleChange = { title = it },
            onDescriptionChange = { description = it },
            bringIntoViewRequester = bringIntoViewRequester
        )
        PreparationFormSection(
            state = state,
            listener = listener,
            bringIntoViewRequester = bringIntoViewRequester
        )
    }
    DiscardConfirmationDialog(showDiscardConfirmation, onDiscard) { showDiscardConfirmation = it }
    FormBackHandler(isEmpty, onDiscard) { showDiscardConfirmation = true }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BasicFormSection(
    title: String,
    description: String,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    bringIntoViewRequester: BringIntoViewRequester,
    modifier: Modifier = Modifier
) {
    FormSection(
        modifier = modifier,
        items = arrayOf(
            {
                FormTextField(
                    value = title,
                    label = R.string.preparation_title_label,
                    errorLabel = R.string.preparation_title_error,
                    isError = title.isBlank(),
                    imeAction = ImeAction.Next,
                    onValueChange = onTitleChange,
                    bringIntoViewRequester = bringIntoViewRequester
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
                    bringIntoViewRequester = bringIntoViewRequester
                )
            }
        )
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PreparationFormSection(
    state: PreparationFormState,
    listener: PreparationFormListener,
    bringIntoViewRequester: BringIntoViewRequester,
    modifier: Modifier = Modifier
) {
    FormSection(
        modifier = modifier,
        items = arrayOf(
            {
                LabeledFormTextFieldColumn(
                    texts = state.thoughts,
                    label = R.string.preparation_thoughts_label,
                    descriptionLabel = R.string.preparation_thoughts_body,
                    onAdd = listener::onThoughtAdded,
                    onDelete = listener::onThoughtRemoved,
                    bringIntoViewRequester = bringIntoViewRequester
                )
                LabeledFormTextFieldColumn(
                    texts = state.interpretations,
                    label = R.string.preparation_interpretations_label,
                    descriptionLabel = R.string.preparation_interpretations_body,
                    onAdd = listener::onInterpretationAdded,
                    onDelete = listener::onInterpretationRemoved,
                    bringIntoViewRequester = bringIntoViewRequester
                )
            },
            {
                LabeledFormTextFieldColumn(
                    texts = state.behaviors,
                    label = R.string.preparation_behaviors_label,
                    descriptionLabel = R.string.preparation_behaviors_body,
                    onAdd = listener::onBehaviorAdded,
                    onDelete = listener::onBehaviorRemoved,
                    bringIntoViewRequester = bringIntoViewRequester
                )
            },
            {
                LabeledFormTextFieldColumn(
                    texts = state.actions,
                    label = R.string.preparation_actions_label,
                    descriptionLabel = R.string.preparation_actions_body,
                    onAdd = listener::onActionAdded,
                    onDelete = listener::onActionRemoved,
                    bringIntoViewRequester = bringIntoViewRequester
                )
            }
        )
    )
}

class PreparationFormState {
    var thoughts = mutableStateListOf<String>()
    var interpretations = mutableStateListOf<String>()
    var behaviors = mutableStateListOf<String>()
    var actions = mutableStateListOf<String>()
}

interface PreparationFormListener {
    fun onThoughtAdded(thought: String)
    fun onThoughtRemoved(thought: String)
    fun onInterpretationAdded(interpretation: String)
    fun onInterpretationRemoved(interpretation: String)
    fun onBehaviorAdded(behavior: String)
    fun onBehaviorRemoved(behavior: String)
    fun onActionAdded(action: String)
    fun onActionRemoved(action: String)
}

class BasePreparationFormListener(private val state: PreparationFormState) : PreparationFormListener {
    override fun onThoughtAdded(thought: String) {
        state.thoughts.add(thought)
    }
    override fun onThoughtRemoved(thought: String) {
        state.thoughts.remove(thought)
    }
    override fun onInterpretationAdded(interpretation: String) {
        state.interpretations.add(interpretation)
    }
    override fun onInterpretationRemoved(interpretation: String) {
        state.interpretations.remove(interpretation)
    }
    override fun onBehaviorAdded(behavior: String) {
        state.behaviors.add(behavior)
    }
    override fun onBehaviorRemoved(behavior: String) {
        state.behaviors.remove(behavior)
    }
    override fun onActionAdded(action: String) {
        state.actions.add(action)
    }
    override fun onActionRemoved(action: String) {
        state.actions.remove(action)
    }
}
