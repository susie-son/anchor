package com.susieson.anchor.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.susieson.anchor.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldColumn(
    modifier: Modifier = Modifier,
    texts: List<String> = mutableListOf(),
    onAdd: (String) -> Unit = {},
    onDelete: (String) -> Unit = {}
) {
    var field by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }

    Column(modifier = modifier) {
        ListItem(headlineContent = {
            BasicTextField(
                value = field,
                onValueChange = { field = it },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    if (field.isNotEmpty()) {
                        onAdd(field)
                        field = ""
                    }
                }),
                interactionSource = interactionSource,
                singleLine = true,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(0.dp, 0.dp, 0.dp, 0.dp),
            ) { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(value = field,
                    innerTextField = innerTextField,
                    enabled = true,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    contentPadding = contentPadding(0.dp, 0.dp, 0.dp, 0.dp),
                    placeholder = { Text("Add") },
                    container = {})
            }
        },
            colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
            trailingContent = {
                IconButton(onClick = {
                    if (field.isNotEmpty()) {
                        onAdd(field)
                        field = ""
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_add),
                        contentDescription = "Add"
                    )
                }
            })
        texts.forEach { text ->
            HorizontalDivider()
            ListItem(headlineContent = { Text(text = text) }, trailingContent = {
                IconButton(onClick = { onDelete(text) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_delete),
                        contentDescription = "Delete"
                    )
                }
            })
        }
    }
}

@Preview
@Composable
fun TextFieldColumnPreview() {
    TextFieldColumn(texts = mutableListOf("1", "2", "3"))
}