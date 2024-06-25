package com.susieson.anchor.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
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
fun TextFieldColumn(
    texts: List<String>,
    bringIntoViewRequester: BringIntoViewRequester,
    onAdd: (String) -> Unit,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var field by rememberSaveable { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier) {
        AddTextField(
            field = field,
            bringIntoViewRequester = bringIntoViewRequester,
            interactionSource = interactionSource,
            coroutineScope = coroutineScope,
            onAdd = onAdd,
            onFieldChange = { field = it }
        )
        TextList(texts, onDelete)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AddTextField(
    field: String,
    bringIntoViewRequester: BringIntoViewRequester,
    interactionSource: MutableInteractionSource,
    coroutineScope: CoroutineScope,
    onFieldChange: (String) -> Unit,
    onAdd: (String) -> Unit,
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
                bringIntoViewRequester = bringIntoViewRequester,
                interactionSource = interactionSource,
                coroutineScope = coroutineScope,
                onValueChange = onFieldChange,
                onAddAndClear = onAddAndClear,
            )
        },
        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        trailingContent = {
            IconButton(onAddAndClear) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(R.string.content_description_add)
                )
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun BasicTextFieldWithDecoration(
    value: String,
    bringIntoViewRequester: BringIntoViewRequester,
    interactionSource: MutableInteractionSource,
    coroutineScope: CoroutineScope,
    onValueChange: (String) -> Unit,
    onAddAndClear: () -> Unit,
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
            placeholder = {},
            container = {}
        )
    }
}

@Composable
private fun TextList(texts: List<String>, onDelete: (String) -> Unit) {
    texts.forEach { text ->
        HorizontalDivider()
        ListItem(
            headlineContent = { Text(text = text) },
            trailingContent = {
                IconButton({ onDelete(text) }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = stringResource(R.string.content_description_delete)
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun TextFieldColumnPreview() {
    TextFieldColumn(
        texts = mutableListOf("1", "2", "3"),
        bringIntoViewRequester = BringIntoViewRequester(),
        onAdd = {},
        onDelete = {}
    )
}
