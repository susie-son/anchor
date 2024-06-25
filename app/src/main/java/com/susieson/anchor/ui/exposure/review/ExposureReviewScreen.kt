package com.susieson.anchor.ui.exposure.review

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
import com.susieson.anchor.model.Emotion
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.model.Review
import com.susieson.anchor.ui.components.DiscardDialog
import com.susieson.anchor.ui.components.FormBackHandler

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExposureReviewScreen(
    userId: String,
    exposure: Exposure,
    viewModel: ExposureReviewViewModel,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDiscardDialog by remember { mutableStateOf(false) }

    val emotionState = remember { mutableStateOf(EmotionFormSectionState()) }
    val ratingState = remember { mutableStateOf(RatingFormSectionState()) }
    val learningsState = remember { mutableStateOf(LearningsFormSectionState()) }

    val emotionForm by emotionState
    val ratingForm by ratingState
    val learningsForm by learningsState

    val emotionsListener = EmotionFormSectionListener(emotionState)
    val ratingsListener = RatingFormSectionListener(ratingState)
    val learningsListener = LearningsFormSectionListener(learningsState)

    val isValid = emotionForm.isValid && ratingForm.isValid && learningsForm.isValid
    val isEmpty = emotionForm.isEmpty && ratingForm.isEmpty && learningsForm.isEmpty

    val emotions = listOf(
        emotionForm.fear to Emotion.FEAR,
        emotionForm.sadness to Emotion.SADNESS,
        emotionForm.anxiety to Emotion.ANXIETY,
        emotionForm.guilt to Emotion.GUILT,
        emotionForm.shame to Emotion.SHAME,
        emotionForm.happiness to Emotion.HAPPINESS
    ).mapNotNull { (exists, emotion) ->
        emotion.takeIf { exists }
    }
    val review = Review(
        emotions,
        emotionForm.thoughts,
        emotionForm.sensations,
        emotionForm.behaviors,
        ratingForm.experiencingRating,
        ratingForm.anchoringRating,
        ratingForm.thinkingRating,
        ratingForm.engagingRating,
        learningsForm.learnings
    )

//    onTopBarChange {
//        CenterAlignedTopAppBar(
//            title = { Text(stringResource(R.string.review_top_bar_title)) },
//            navigationIcon = {
//                IconButton({
//                    if (isEmpty) onDiscard() else {
//                        showDiscardDialog = true
//                    }
//                }) {
//                    Icon(Icons.Default.Close, stringResource(R.string.content_description_close))
//                }
//            },
//            actions = {
//                IconButton(
//                    onClick = {
//                        addReview(userId, exposureId, review)
//                    },
//                    enabled = isValid
//                ) {
//                    Icon(Icons.Default.Done, stringResource(R.string.content_description_done))
//                }
//            },
//            modifier = modifier,
//        )
//    }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        val bringIntoViewRequester = remember { BringIntoViewRequester() }
        EmotionFormSection(emotionForm, emotionsListener, bringIntoViewRequester)
        RatingFormSection(ratingForm, ratingsListener)
        LearningsFormSection(learningsForm, learningsListener, bringIntoViewRequester)
    }
    DiscardDialog(showDiscardDialog, onNavigateUp) { showDiscardDialog = it }
    FormBackHandler(isEmpty, onNavigateUp) { showDiscardDialog = true }
}
