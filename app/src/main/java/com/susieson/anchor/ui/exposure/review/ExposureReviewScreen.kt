package com.susieson.anchor.ui.exposure.review

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.susieson.anchor.R
import com.susieson.anchor.model.Emotion
import com.susieson.anchor.ui.components.Action
import com.susieson.anchor.ui.components.BodyText
import com.susieson.anchor.ui.components.ErrorBodyText
import com.susieson.anchor.ui.components.ErrorLabelText
import com.susieson.anchor.ui.components.ErrorText
import com.susieson.anchor.ui.components.FormDiscardHandler
import com.susieson.anchor.ui.components.LabelText
import com.susieson.anchor.ui.components.LabeledItem
import com.susieson.anchor.ui.components.LabeledItemWithSupporting
import com.susieson.anchor.ui.components.SliderWithLabel
import com.susieson.anchor.ui.components.TextFieldColumn
import com.susieson.anchor.ui.components.onClose
import com.susieson.anchor.ui.components.onDone

@Composable
fun ExposureReviewScreen(
    viewModel: ExposureReviewViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val emotions by remember { viewModel.emotions }
    val thoughts = remember { viewModel.thoughts }
    val sensations = remember { viewModel.sensations }
    val behaviors = remember { viewModel.behaviors }
    val experiencingRating by remember { viewModel.experiencing }
    val anchoringRating by remember { viewModel.anchoring }
    val thinkingRating by remember { viewModel.thinking }
    val engagingRating by remember { viewModel.engaging }
    val learnings by remember { viewModel.learnings }

    val showDiscardDialog by remember { viewModel.showDiscardDialog }

    val isValid by remember { viewModel.isValid }
    val isEmpty by remember { viewModel.isEmpty }

    Scaffold(
        topBar = {
            ExposureReviewTopBar(
                onClose = {
                    onClose(isEmpty, navController::navigateUp, viewModel::onShowDiscardDialogChange)
                },
                onComplete = { onDone(viewModel::addReview, navController::navigateUp) },
                isValid = isValid
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            EmotionsSection(
                emotions = emotions,
                thoughts = thoughts,
                sensations = sensations,
                behaviors = behaviors,
                onEmotionChange = viewModel::onEmotionChanged,
                onThoughtChange = viewModel::onThoughtChange,
                onSensationChange = viewModel::onSensationChange,
                onBehaviorChange = viewModel::onBehaviorChange,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            RatingsSection(
                experiencingRating = experiencingRating,
                anchoringRating = anchoringRating,
                thinkingRating = thinkingRating,
                engagingRating = engagingRating,
                onExperiencingRatingChange = viewModel::onExperiencingRatingChanged,
                onAnchoringRatingChange = viewModel::onAnchoringRatingChanged,
                onThinkingRatingChange = viewModel::onThinkingRatingChanged,
                onEngagingRatingChange = viewModel::onEngagingRatingChanged,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            LearningsSection(
                learnings = learnings,
                onLearningsChange = viewModel::onLearningsChange,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
    FormDiscardHandler(
        isEmpty = isEmpty,
        showDiscardDialog = showDiscardDialog,
        onDiscard = navController::navigateUp,
        onShowDiscardDialog = viewModel::onShowDiscardDialogChange
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExposureReviewTopBar(
    onClose: () -> Unit,
    onComplete: () -> Unit,
    isValid: Boolean,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.review_top_bar_title)) },
        navigationIcon = {
            IconButton(onClose) {
                Icon(Icons.Default.Close, stringResource(R.string.content_description_close))
            }
        },
        actions = {
            IconButton(onComplete, enabled = isValid) {
                Icon(Icons.Default.Done, stringResource(R.string.content_description_done))
            }
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EmotionsSection(
    emotions: Map<Emotion, Boolean>,
    thoughts: List<String>,
    sensations: List<String>,
    behaviors: List<String>,
    onEmotionChange: (Emotion) -> Unit,
    onThoughtChange: (Action, String) -> Unit,
    onSensationChange: (Action, String) -> Unit,
    onBehaviorChange: (Action, String) -> Unit,
    modifier: Modifier = Modifier
) {
    data class EmotionCategory(
        @StringRes val label: Int,
        @StringRes val body: Int,
        val items: List<String>,
        val onChange: (Action, String) -> Unit
    )
    val items = listOf(
        EmotionCategory(
            R.string.review_thoughts_label,
            R.string.review_thoughts_body,
            thoughts,
            onThoughtChange
        ),
        EmotionCategory(
            R.string.review_sensations_label,
            R.string.review_sensations_body,
            sensations,
            onSensationChange
        ),
        EmotionCategory(
            R.string.review_behaviors_label,
            R.string.review_behaviors_body,
            behaviors,
            onBehaviorChange
        )
    )
    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        LabeledItem({ LabelText(stringResource(R.string.review_emotions_label)) }) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                emotions.forEach { (emotion, selected) ->
                    FilterChip(
                        selected = selected,
                        onClick = { onEmotionChange(emotion) },
                        label = { Text(stringResource(emotion.label)) }
                    )
                }
            }
        }
        items.forEach { category ->
            LabeledItemWithSupporting(
                label = { LabelText(stringResource(category.label)) },
                supporting = { ErrorBodyText(stringResource(category.body), category.items.isEmpty()) }
            ) {
                TextFieldColumn(
                    texts = category.items,
                    onAdd = { category.onChange(Action.ADD, it) },
                    onRemove = { category.onChange(Action.REMOVE, it) }
                )
            }
        }
    }
}

@Composable
private fun RatingsSection(
    experiencingRating: Float,
    anchoringRating: Float,
    thinkingRating: Float,
    engagingRating: Float,
    onExperiencingRatingChange: (Float) -> Unit,
    onAnchoringRatingChange: (Float) -> Unit,
    onThinkingRatingChange: (Float) -> Unit,
    onEngagingRatingChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    data class RatingCategory(
        @StringRes
        val label: Int,
        val rating: Float,
        val onRatingChange: (Float) -> Unit
    )
    val items = listOf(
        RatingCategory(R.string.review_experiencing_label, experiencingRating, onExperiencingRatingChange),
        RatingCategory(R.string.review_anchoring_label, anchoringRating, onAnchoringRatingChange),
        RatingCategory(R.string.review_thinking_label, thinkingRating, onThinkingRatingChange),
        RatingCategory(R.string.review_engaging_label, engagingRating, onEngagingRatingChange)
    )
    Column(modifier) {
        LabeledItem({ LabelText(stringResource(R.string.review_effectiveness_label)) }) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items.forEach { category ->
                    LabeledItem(
                        {
                            ErrorLabelText(stringResource(category.label), category.rating == 0f)
                        }
                    ) {
                        SliderWithLabel(
                            value = category.rating,
                            onValueChange = category.onRatingChange,
                            valueRange = 0f..10f,
                            steps = 9,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LearningsSection(
    learnings: String,
    onLearningsChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LabeledItemWithSupporting(
        label = { LabelText(stringResource(R.string.review_learnings_label)) },
        supporting = { BodyText(stringResource(R.string.review_learnings_body)) },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = learnings,
            isError = learnings.isBlank(),
            supportingText = {
                ErrorText(stringResource(R.string.review_learnings_error), learnings.isBlank())
            },
            onValueChange = onLearningsChange,
            singleLine = false,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
