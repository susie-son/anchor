package com.susieson.anchor.ui.exposure

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import com.susieson.anchor.R
import com.susieson.anchor.model.Emotion
import com.susieson.anchor.model.Review
import com.susieson.anchor.ui.components.AnchorIconButton
import com.susieson.anchor.ui.components.AnchorScaffold
import com.susieson.anchor.ui.components.AnchorTopAppBar
import com.susieson.anchor.ui.components.DiscardConfirmationDialog
import com.susieson.anchor.ui.components.FormRatingItem
import com.susieson.anchor.ui.components.FormSection
import com.susieson.anchor.ui.components.FormSelectFilterItem
import com.susieson.anchor.ui.components.FormTextField
import com.susieson.anchor.ui.components.LabeledFormSection
import com.susieson.anchor.ui.components.LabeledFormTextFieldColumn

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReviewForm(
    userId: String,
    exposureId: String,
    onDiscard: () -> Unit,
    addReview: (String, String, Review) -> Unit,
    setScaffold: (AnchorScaffold) -> Unit,
    modifier: Modifier = Modifier
) {
    var fear by remember { mutableStateOf(false) }
    var sadness by remember { mutableStateOf(false) }
    var anxiety by remember { mutableStateOf(false) }
    var guilt by remember { mutableStateOf(false) }
    var shame by remember { mutableStateOf(false) }
    var happiness by remember { mutableStateOf(false) }

    val thoughts = remember { mutableStateListOf<String>() }
    val sensations = remember { mutableStateListOf<String>() }
    val behaviors = remember { mutableStateListOf<String>() }

    var experiencingRating by remember { mutableFloatStateOf(0f) }
    var anchoringRating by remember { mutableFloatStateOf(0f) }
    var thinkingRating by remember { mutableFloatStateOf(0f) }
    var engagingRating by remember { mutableFloatStateOf(0f) }

    var learnings by remember { mutableStateOf("") }

    var showDiscardConfirmation by remember { mutableStateOf(false) }

    val emotionsFilters =
        mapOf(
            R.string.review_fear_chip to fear,
            R.string.review_sadness_chip to sadness,
            R.string.review_anxiety_chip to anxiety,
            R.string.review_guilt_chip to guilt,
            R.string.review_shame_chip to shame,
            R.string.review_happiness_chip to happiness
        )

    val isValid =
        emotionsFilters.values.contains(true) &&
            thoughts.isNotEmpty() &&
            sensations.isNotEmpty() &&
            behaviors.isNotEmpty() &&
            experiencingRating > 0 &&
            anchoringRating > 0 &&
            thinkingRating > 0 &&
            engagingRating > 0 &&
            learnings.isNotEmpty()
    val isEmpty =
        emotionsFilters.values.all { !it } &&
            thoughts.isEmpty() &&
            sensations.isEmpty() &&
            behaviors.isEmpty() &&
            experiencingRating == 0f &&
            anchoringRating == 0f &&
            thinkingRating == 0f &&
            engagingRating == 0f &&
            learnings.isEmpty()

    setScaffold(
        AnchorScaffold(
            topAppBar = AnchorTopAppBar(
                title = R.string.review_top_bar_title,
                navigationIcon = AnchorIconButton(
                    onClick = {
                        if (isEmpty) {
                            onDiscard()
                        } else {
                            showDiscardConfirmation = true
                        }
                    },
                    icon = Icons.Default.Close,
                    contentDescription = R.string.content_description_close
                ),
                actions = listOf(
                    AnchorIconButton(
                        onClick = {
                            val emotions =
                                listOf(
                                    fear to Emotion.FEAR,
                                    sadness to Emotion.SADNESS,
                                    anxiety to Emotion.ANXIETY,
                                    guilt to Emotion.GUILT,
                                    shame to Emotion.SHAME,
                                    happiness to Emotion.HAPPINESS
                                )
                                    .filter { it.first }
                                    .map { it.second }
                            val review = Review(
                                emotions,
                                thoughts,
                                sensations,
                                behaviors,
                                experiencingRating,
                                anchoringRating,
                                thinkingRating,
                                engagingRating,
                                learnings
                            )
                            addReview(userId, exposureId, review)
                        },
                        icon = Icons.Default.Done,
                        contentDescription = R.string.content_description_done,
                        enabled = isValid
                    )
                )
            )
        )
    )

    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        val bringIntoViewRequester = remember { BringIntoViewRequester() }

        FormSection(
            {
                FormSelectFilterItem(
                    label = R.string.review_emotions_label,
                    filters = emotionsFilters,
                    onFilterChange = {
                        when (it) {
                            R.string.review_fear_chip -> {
                                fear = !fear
                            }

                            R.string.review_sadness_chip -> {
                                sadness = !sadness
                            }

                            R.string.review_anxiety_chip -> {
                                anxiety = !anxiety
                            }

                            R.string.review_guilt_chip -> {
                                guilt = !guilt
                            }

                            R.string.review_shame_chip -> {
                                shame = !shame
                            }

                            R.string.review_happiness_chip -> {
                                happiness = !happiness
                            }
                        }
                    }
                )
            }
        )
        FormSection(
            {
                LabeledFormTextFieldColumn(
                    texts = thoughts,
                    label = R.string.review_thoughts_label,
                    descriptionLabel = R.string.review_thoughts_body,
                    onAdd = { thoughts.add(it) },
                    onDelete = { thoughts.remove(it) },
                    bringIntoViewRequester = bringIntoViewRequester
                )
            },
            {
                LabeledFormTextFieldColumn(
                    texts = sensations,
                    label = R.string.review_sensations_label,
                    descriptionLabel = R.string.review_sensations_body,
                    onAdd = { sensations.add(it) },
                    onDelete = { sensations.remove(it) },
                    bringIntoViewRequester = bringIntoViewRequester
                )
            },
            {
                LabeledFormTextFieldColumn(
                    texts = behaviors,
                    label = R.string.review_behaviors_label,
                    descriptionLabel = R.string.review_behaviors_body,
                    onAdd = { behaviors.add(it) },
                    onDelete = { behaviors.remove(it) },
                    bringIntoViewRequester = bringIntoViewRequester
                )
            }
        )
        LabeledFormSection(
            label = R.string.review_effectiveness_label,
            descriptionLabel = null,
            {
                FormRatingItem(
                    label = R.string.review_experiencing_label,
                    value = experiencingRating,
                    onValueChange = { experiencingRating = it },
                )
            },
            {
                FormRatingItem(
                    label = R.string.review_anchoring_label,
                    value = anchoringRating,
                    onValueChange = { anchoringRating = it },
                )
            },
            {
                FormRatingItem(
                    label = R.string.review_thinking_label,
                    value = thinkingRating,
                    onValueChange = { thinkingRating = it },
                )
            },
            {
                FormRatingItem(
                    label = R.string.review_engaging_label,
                    value = engagingRating,
                    onValueChange = { engagingRating = it },
                )
            }
        )
        LabeledFormSection(
            label = R.string.review_learnings_label,
            descriptionLabel = R.string.review_learnings_body,
            {
                FormTextField(
                    value = learnings,
                    label = null,
                    errorLabel = R.string.review_learnings_error,
                    isError = learnings.isEmpty(),
                    imeAction = ImeAction.Done,
                    onValueChange = { learnings = it },
                    singleLine = false,
                    bringIntoViewRequester = bringIntoViewRequester
                )
            }
        )
    }

    if (showDiscardConfirmation) {
        DiscardConfirmationDialog(
            onDismiss = { showDiscardConfirmation = false },
            onConfirm = {
                showDiscardConfirmation = false
                onDiscard()
            }
        )
    }

    BackHandler {
        if (isEmpty) {
            onDiscard()
        } else {
            showDiscardConfirmation = true
        }
    }
}
