package com.susieson.anchor.ui.exposure

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.relocation.BringIntoViewRequester
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
import com.susieson.anchor.ui.AnchorScreenState
import com.susieson.anchor.ui.components.AnchorFormState
import com.susieson.anchor.ui.components.AnchorTopAppBarState
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
    onDiscard: () -> Unit,
    onSubmit: () -> Unit,
    setScreenState: (AnchorScreenState) -> Unit,
    modifier: Modifier = Modifier
) {
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

    setScreenState(
        AnchorScreenState(
            topAppBarState = AnchorTopAppBarState(R.string.review_top_bar_title),
            formState = AnchorFormState(
                isValid = isValid,
                isEmpty = isEmpty,
                onDiscard = { showDiscardConfirmation = true },
                onSubmit = onSubmit
            ),
            canNavigateUp = true,
        )
    )

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        val bringIntoViewRequester = remember { BringIntoViewRequester() }

        FormSection(
            modifier = modifier,
            {
                FormSelectFilterItem(
                    label = R.string.review_emotions_label,
                    filters = emotionsFilters,
                    onFilterChange = {
                        when (it) {
                            R.string.review_fear_chip -> setFear()
                            R.string.review_sadness_chip -> setSadness()
                            R.string.review_anxiety_chip -> setAnxiety()
                            R.string.review_guilt_chip -> setGuilt()
                            R.string.review_shame_chip -> setShame()
                            R.string.review_happiness_chip -> setHappiness()
                        }
                    }
                )
            }
        )
        FormSection(
            modifier = modifier,
            {
                LabeledFormTextFieldColumn(
                    texts = thoughts,
                    label = R.string.review_thoughts_label,
                    descriptionLabel = R.string.review_thoughts_body,
                    onAdd = addThought,
                    onDelete = removeThought,
                    modifier = modifier,
                    bringIntoViewRequester = bringIntoViewRequester
                )
            },
            {
                LabeledFormTextFieldColumn(
                    texts = sensations,
                    label = R.string.review_sensations_label,
                    descriptionLabel = R.string.review_sensations_body,
                    onAdd = addSensation,
                    onDelete = removeSensation,
                    modifier = modifier,
                    bringIntoViewRequester = bringIntoViewRequester
                )
            },
            {
                LabeledFormTextFieldColumn(
                    texts = behaviors,
                    label = R.string.review_behaviors_label,
                    descriptionLabel = R.string.review_behaviors_body,
                    onAdd = addBehavior,
                    onDelete = removeBehavior,
                    modifier = modifier,
                    bringIntoViewRequester = bringIntoViewRequester
                )
            }
        )
        LabeledFormSection(
            label = R.string.review_effectiveness_label,
            descriptionLabel = null,
            modifier = modifier,
            {
                FormRatingItem(
                    label = R.string.review_experiencing_label,
                    value = experiencingRating,
                    onValueChange = setExperiencingRating,
                    modifier = modifier
                )
            },
            {
                FormRatingItem(
                    label = R.string.review_anchoring_label,
                    value = anchoringRating,
                    onValueChange = setAnchoringRating,
                    modifier = modifier
                )
            },
            {
                FormRatingItem(
                    label = R.string.review_thinking_label,
                    value = thinkingRating,
                    onValueChange = setThinkingRating,
                    modifier = modifier
                )
            },
            {
                FormRatingItem(
                    label = R.string.review_engaging_label,
                    value = engagingRating,
                    onValueChange = setEngagingRating,
                    modifier = modifier
                )
            }
        )
        LabeledFormSection(
            label = R.string.review_learnings_label,
            descriptionLabel = R.string.review_learnings_body,
            modifier = modifier,
            {
                FormTextField(
                    value = learnings,
                    label = null,
                    errorLabel = R.string.review_learnings_error,
                    isError = learnings.isEmpty(),
                    imeAction = ImeAction.Done,
                    onValueChange = setLearnings,
                    singleLine = false,
                    modifier = modifier,
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
}
