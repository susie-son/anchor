package com.susieson.anchor.ui.components.form

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.susieson.anchor.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LabeledFormTextFieldColumn(
    texts: List<String>,
    @StringRes
    label: Int,
    onAdd: (String) -> Unit,
    onDelete: (String) -> Unit,
    bringIntoViewRequester: BringIntoViewRequester,
    modifier: Modifier = Modifier,
    @StringRes
    descriptionLabel: Int? = null
) {
    val isError = texts.isEmpty()

    val labelItem: @Composable () -> Unit = {
        Text(
            stringResource(label),
            style = MaterialTheme.typography.labelLarge
        )
    }
    val descriptionItem: @Composable () -> Unit = {
        descriptionLabel?.let {
            Text(
                text = stringResource(it),
                style = MaterialTheme.typography.bodySmall,
                color = if (isError) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        labelItem()
        descriptionItem()
        FormTextFieldColumn(
            texts = texts,
            onAdd = onAdd,
            onDelete = onDelete,
            bringIntoViewRequester = bringIntoViewRequester
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FormTextFieldColumn(
    texts: List<String>,
    onAdd: (String) -> Unit,
    onDelete: (String) -> Unit,
    bringIntoViewRequester: BringIntoViewRequester,
    modifier: Modifier = Modifier
) {
    var field by rememberSaveable { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier) {
        FormAddTextField(field, onAdd, interactionSource, bringIntoViewRequester, coroutineScope) {
            field = it
        }
        FormTextList(texts, onDelete)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FormAddTextField(
    field: String,
    onAdd: (String) -> Unit,
    interactionSource: MutableInteractionSource,
    bringIntoViewRequester: BringIntoViewRequester,
    coroutineScope: CoroutineScope,
    onFieldChange: (String) -> Unit,
) {
    val onAddAndClear = {
        if (field.isNotEmpty()) {
            onAdd(field)
            onFieldChange("")
        }
    }
    ListItem(
        headlineContent = {
            BasicTextFieldWithDecoration(
                value = field,
                onValueChange = onFieldChange,
                onAddAndClear = onAddAndClear,
                interactionSource = interactionSource,
                bringIntoViewRequester = bringIntoViewRequester,
                coroutineScope = coroutineScope
            )
        },
        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        trailingContent = {
            IconButton(onClick = onAddAndClear) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.content_description_add))
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun BasicTextFieldWithDecoration(
    value: String,
    onValueChange: (String) -> Unit,
    onAddAndClear: () -> Unit,
    interactionSource: MutableInteractionSource,
    bringIntoViewRequester: BringIntoViewRequester,
    coroutineScope: CoroutineScope
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
        keyboardActions = KeyboardActions(onSend = { onAddAndClear() }),
        interactionSource = interactionSource,
        singleLine = true,
        textStyle = TextStyle.Default.copy(color = OutlinedTextFieldDefaults.colors().focusedTextColor),
        cursorBrush = SolidColor(OutlinedTextFieldDefaults.colors().cursorColor),
        modifier = Modifier
            .bringIntoViewRequester(bringIntoViewRequester)
            .onFocusChanged {
                if (it.isFocused) {
                    coroutineScope.launch { bringIntoViewRequester.bringIntoView() }
                }
            }
            .focusTarget()
    ) { innerTextField ->
        OutlinedTextFieldDefaults.DecorationBox(
            value = value,
            innerTextField = innerTextField,
            enabled = true,
            singleLine = true,
            visualTransformation = VisualTransformation.None,
            interactionSource = interactionSource,
            contentPadding = OutlinedTextFieldDefaults.contentPadding(0.dp),
            placeholder = { Text(stringResource(R.string.text_field_placeholder)) },
            container = {}
        )
    }
}

@Composable
private fun FormTextList(texts: List<String>, onDelete: (String) -> Unit) {
    texts.forEach { text ->
        HorizontalDivider()
        ListItem(
            headlineContent = { Text(text = text) },
            trailingContent = {
                IconButton(onClick = { onDelete(text) }) {
                    Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.content_description_delete))
                }
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun FormTextFieldColumnPreview() {
    FormTextFieldColumn(
        texts = mutableListOf("1", "2", "3"),
        onAdd = {},
        onDelete = {},
        bringIntoViewRequester = BringIntoViewRequester()
    )
}