package com.susieson.anchor.ui.review

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.susieson.anchor.model.Review
import com.susieson.anchor.ui.components.DiscardDialog
import com.susieson.anchor.ui.components.LabeledSlider
import com.susieson.anchor.ui.components.TextFieldColumn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewTopBar(
    modifier: Modifier = Modifier,
    isValid: Boolean,
    isEmpty: Boolean,
    onBack: () -> Unit,
    onDiscard: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.review_top_bar_title)) },
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ReviewScreen(
    modifier: Modifier = Modifier,
    userId: String,
    exposureId: String,
    onBack: () -> Unit = {},
    reviewViewModel: ReviewViewModel = hiltViewModel()
) {
    var fear by rememberSaveable { mutableStateOf(false) }
    var sadness by rememberSaveable { mutableStateOf(false) }
    var anxiety by rememberSaveable { mutableStateOf(false) }
    var guilt by rememberSaveable { mutableStateOf(false) }
    var shame by rememberSaveable { mutableStateOf(false) }
    var happiness by rememberSaveable { mutableStateOf(false) }

    val emotions = listOfNotNull(
        if (fear) stringResource(R.string.review_emotions_fear) else null,
        if (sadness) stringResource(R.string.review_emotions_sadness) else null,
        if (anxiety) stringResource(R.string.review_emotions_anxiety) else null,
        if (guilt) stringResource(R.string.review_emotions_guilt) else null,
        if (shame) stringResource(R.string.review_emotions_shame) else null,
        if (happiness) stringResource(R.string.review_emotions_happiness) else null
    )

    var thoughts by rememberSaveable { mutableStateOf<List<String>>(emptyList()) }
    var sensations by rememberSaveable { mutableStateOf<List<String>>(emptyList()) }
    var behaviors by rememberSaveable { mutableStateOf<List<String>>(emptyList()) }

    var experiencingRating by rememberSaveable { mutableFloatStateOf(0f) }
    var anchoringRating by rememberSaveable { mutableFloatStateOf(0f) }
    var thinkingRating by rememberSaveable { mutableFloatStateOf(0f) }
    var engagingRating by rememberSaveable { mutableFloatStateOf(0f) }

    var learnings by rememberSaveable { mutableStateOf("") }

    var openDiscardDialog by rememberSaveable { mutableStateOf(false) }

    val emotionsError = emotions.isEmpty()

    val thoughtsError = thoughts.isEmpty()
    val sensationsError = sensations.isEmpty()
    val behaviorsError = behaviors.isEmpty()

    val experiencingError = experiencingRating == 0f
    val anchoringError = anchoringRating == 0f
    val thinkingError = thinkingRating == 0f
    val engagingError = engagingRating == 0f

    val learningsError = learnings.isBlank()

    val isValid = !emotionsError && !thoughtsError && !sensationsError && !behaviorsError && !experiencingError && !anchoringError && !thinkingError && !engagingError && !learningsError
    val isEmpty =
        !fear && !sadness && !anxiety && !guilt && !shame && !happiness &&
                thoughts.isEmpty() && sensations.isEmpty() && behaviors.isEmpty() &&
                experiencingRating == 0f && anchoringRating == 0f && thinkingRating == 0f && engagingRating == 0f &&
                learnings.isBlank()

    if (openDiscardDialog) {
        DiscardDialog(onConfirm = onBack, onDismiss = { openDiscardDialog = false })
    }

    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        ReviewTopBar(
            isValid = isValid,
            isEmpty = isEmpty,
            onBack = onBack,
            onDiscard = { openDiscardDialog = true },
            onConfirm = {
                val review = Review(
                    emotions = emotions,
                    thoughts = thoughts,
                    sensations = sensations,
                    behaviors = behaviors,
                    experiencing = experiencingRating,
                    anchoring = anchoringRating,
                    thinking = thinkingRating,
                    engaging = engagingRating,
                    learnings = learnings
                )
                reviewViewModel.add(userId, exposureId, review)
                onBack()
            }
        )
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            Text(
                text = stringResource(R.string.review_emotions_label),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(bottom = 8.dp),
                color = if (emotionsError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = fear,
                    onClick = { fear = !fear },
                    label = { Text(stringResource(R.string.review_fear_chip)) }
                )
                FilterChip(
                    selected = sadness,
                    onClick = { sadness = !sadness },
                    label = { Text(stringResource(R.string.review_sadness_chip)) }
                )
                FilterChip(
                    selected = anxiety,
                    onClick = { anxiety = !anxiety },
                    label = { Text(stringResource(R.string.review_anxiety_chip)) }
                )
                FilterChip(
                    selected = guilt,
                    onClick = { guilt = !guilt },
                    label = { Text(stringResource(R.string.review_guilt_chip)) }
                )
                FilterChip(
                    selected = shame,
                    onClick = { shame = !shame },
                    label = { Text(stringResource(R.string.review_shame_chip)) }
                )
                FilterChip(
                    selected = happiness,
                    onClick = { happiness = !happiness },
                    label = { Text(stringResource(R.string.review_happiness_chip)) }
                )
            }
            Text(
                text = stringResource(R.string.review_thoughts_label),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
                color = if (thoughtsError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
            )
            TextFieldColumn(texts = thoughts,
                onAdd = { field -> thoughts = listOf(field) + thoughts },
                onDelete = { text -> thoughts = thoughts.filter { it != text } })
            Text(
                text = stringResource(R.string.review_sensations_label),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
                color = if (sensationsError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
            )
            TextFieldColumn(texts = sensations,
                onAdd = { field -> sensations = listOf(field) + sensations },
                onDelete = { text -> sensations = sensations.filter { it != text } })
            Text(
                text = stringResource(R.string.review_behaviors_label),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
                color = if (behaviorsError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
            )
            TextFieldColumn(texts = behaviors,
                onAdd = { field -> behaviors = listOf(field) + behaviors },
                onDelete = { text -> behaviors = behaviors.filter { it != text } })
            Text(
                text = stringResource(R.string.review_effectiveness_label),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
            )
            Text(
                text = stringResource(R.string.review_experiencing_label),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(bottom = 8.dp),
                color = if (experiencingError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
            )
            LabeledSlider(
                value = experiencingRating,
                onValueChange = { experiencingRating = it },
                valueRange = 0f..10f,
                steps = 9,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = stringResource(R.string.review_anchoring_label),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(bottom = 8.dp),
                color = if (anchoringError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
            )
            LabeledSlider(
                value = anchoringRating,
                onValueChange = { anchoringRating = it },
                valueRange = 0f..10f,
                steps = 9,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = stringResource(R.string.review_thinking_label),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(bottom = 8.dp),
                color = if (thinkingError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
            )
            LabeledSlider(
                value = thinkingRating,
                onValueChange = { thinkingRating = it },
                valueRange = 0f..10f,
                steps = 9,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = stringResource(R.string.review_engaging_label),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(bottom = 8.dp),
                color = if (engagingError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
            )
            LabeledSlider(
                value = engagingRating,
                onValueChange = { engagingRating = it },
                valueRange = 0f..10f,
                steps = 9,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = stringResource(R.string.review_learnings_label),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 24.dp)
            )
            Text(
                text = stringResource(R.string.review_learnings_body),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )
            OutlinedTextField(
                value = learnings,
                onValueChange = { learnings = it },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                isError = learningsError,
                supportingText = { if (learningsError) Text(stringResource(R.string.review_learnings_error)) }
            )
        }
    }
}