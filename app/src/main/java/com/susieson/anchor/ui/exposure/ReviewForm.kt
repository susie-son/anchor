package com.susieson.anchor.ui.exposure

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
import com.susieson.anchor.ui.components.LabeledSlider
import com.susieson.anchor.ui.components.TextFieldColumn

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ReviewForm(
    fear: Boolean,
    sadness: Boolean,
    anxiety: Boolean,
    guilt: Boolean,
    shame: Boolean,
    happiness: Boolean,
    thoughts: List<String>,
    sensations: List<String>,
    behaviors: List<String>,
    experiencingRating: Float,
    anchoringRating: Float,
    thinkingRating: Float,
    engagingRating: Float,
    learnings: String,
    setFear: () -> Unit,
    setSadness: () -> Unit,
    setAnxiety: () -> Unit,
    setGuilt: () -> Unit,
    setShame: () -> Unit,
    setHappiness: () -> Unit,
    addThought: (String) -> Unit,
    addSensation: (String) -> Unit,
    addBehavior: (String) -> Unit,
    setExperiencingRating: (Float) -> Unit,
    setAnchoringRating: (Float) -> Unit,
    setThinkingRating: (Float) -> Unit,
    setEngagingRating: (Float) -> Unit,
    setLearnings: (String) -> Unit,
    removeThought: (String) -> Unit,
    removeSensation: (String) -> Unit,
    removeBehavior: (String) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit,
    setTopAppBarState: (TopAppBarState) -> Unit,
    modifier: Modifier = Modifier,
) {
    var openDiscardDialog by remember { mutableStateOf(false) }

    val emotionsError = !fear && !sadness && !anxiety && !guilt && !shame && !happiness

    val thoughtsError = thoughts.isEmpty()
    val sensationsError = sensations.isEmpty()
    val behaviorsError = behaviors.isEmpty()

    val experiencingError = experiencingRating == 0f
    val anchoringError = anchoringRating == 0f
    val thinkingError = thinkingRating == 0f
    val engagingError = engagingRating == 0f

    val learningsError = learnings.isBlank()

    val isValid =
        !emotionsError && !thoughtsError && !sensationsError && !behaviorsError && !experiencingError && !anchoringError && !thinkingError && !engagingError && !learningsError
    val isEmpty =
        !fear && !sadness && !anxiety && !guilt && !shame && !happiness &&
                thoughts.isEmpty() && sensations.isEmpty() && behaviors.isEmpty() &&
                experiencingRating == 0f && anchoringRating == 0f && thinkingRating == 0f && engagingRating == 0f &&
                learnings.isBlank()

    if (openDiscardDialog) {
        DiscardDialog(
            onConfirm = {
                openDiscardDialog = false
                onBack()
            },
            onDismiss = {
                openDiscardDialog = false
            }
        )
    }

    setTopAppBarState(
        TopAppBarState(
            title = R.string.review_top_bar_title,
            onBack = onBack,
            formState = FormState(
                isValid = isValid,
                isEmpty = isEmpty,
                onDiscard = { openDiscardDialog = true },
                onConfirm = {
                    onNext()
                    onBack()
                }
            )
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
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
                onClick = setFear,
                label = { Text(stringResource(R.string.review_fear_chip)) }
            )
            FilterChip(
                selected = sadness,
                onClick = setSadness,
                label = { Text(stringResource(R.string.review_sadness_chip)) }
            )
            FilterChip(
                selected = anxiety,
                onClick = setAnxiety,
                label = { Text(stringResource(R.string.review_anxiety_chip)) }
            )
            FilterChip(
                selected = guilt,
                onClick = setGuilt,
                label = { Text(stringResource(R.string.review_guilt_chip)) }
            )
            FilterChip(
                selected = shame,
                onClick = setShame,
                label = { Text(stringResource(R.string.review_shame_chip)) }
            )
            FilterChip(
                selected = happiness,
                onClick = setHappiness,
                label = { Text(stringResource(R.string.review_happiness_chip)) }
            )
        }
        Text(
            text = stringResource(R.string.review_thoughts_label),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
            color = if (thoughtsError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        )
        TextFieldColumn(thoughts, addThought, removeThought)
        Text(
            text = stringResource(R.string.review_sensations_label),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
            color = if (sensationsError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        )
        TextFieldColumn(sensations, addSensation, removeSensation)
        Text(
            text = stringResource(R.string.review_behaviors_label),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
            color = if (behaviorsError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        )
        TextFieldColumn(behaviors, addBehavior, removeBehavior)
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
            onValueChange = setExperiencingRating,
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
            onValueChange = setAnchoringRating,
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
            onValueChange = setThinkingRating,
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
            onValueChange = setEngagingRating,
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
            onValueChange = setLearnings,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            isError = learningsError,
            supportingText = { if (learningsError) Text(stringResource(R.string.review_learnings_error)) }
        )
    }
}