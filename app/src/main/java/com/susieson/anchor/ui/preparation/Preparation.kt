package com.susieson.anchor.ui.preparation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.susieson.anchor.R
import com.susieson.anchor.ui.components.TextFieldColumn
import com.susieson.anchor.ui.theme.AnchorTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreparationTopBar(modifier: Modifier = Modifier, onBack: () -> Unit) {
    TopAppBar(title = { Text(text = stringResource(R.string.preparation_top_bar_title)) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_close),
                    contentDescription = "Close"
                )
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
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
fun Preparation(modifier: Modifier = Modifier, onBack: () -> Unit = {}) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var thoughts by remember { mutableStateOf(listOf<String>()) }
    var interpretations by remember { mutableStateOf(listOf<String>()) }
    var behaviors by remember { mutableStateOf(listOf<String>()) }
    var actions by remember { mutableStateOf(listOf<String>()) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { PreparationTopBar(onBack = onBack) }) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(value = title,
                label = { Text(stringResource(R.string.preparation_title_label)) },
                onValueChange = { title = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description,
                label = { Text(stringResource(R.string.preparation_description_label)) },
                onValueChange = { description = it },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )
            Text(
                text = stringResource(R.string.preparation_thoughts_label),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 24.dp)
            )
            Text(
                text = stringResource(R.string.preparation_thoughts_body),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
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
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
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
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
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
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )
            TextFieldColumn(texts = actions,
                onAdd = { field -> actions = listOf(field) + actions },
                onDelete = { text -> actions = actions.filter { it != text } })
        }
    }
}

@Preview
@Composable
fun PreparationPreview() {
    AnchorTheme {
        Preparation()
    }
}