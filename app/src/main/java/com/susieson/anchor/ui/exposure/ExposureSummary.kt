package com.susieson.anchor.ui.exposure

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.susieson.anchor.R
import com.susieson.anchor.model.Emotion
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.model.Preparation
import com.susieson.anchor.model.Review
import com.susieson.anchor.ui.AnchorScreenState
import com.susieson.anchor.ui.components.AnchorTopAppBarState
import com.susieson.anchor.ui.components.CommaSeparatedListSummaryItem
import com.susieson.anchor.ui.components.LineSeparatedListSummaryItem
import com.susieson.anchor.ui.components.RatingSummaryItem
import com.susieson.anchor.ui.components.SummarySection
import com.susieson.anchor.ui.components.TextSummaryItem
import java.text.DateFormat

@Composable
fun ExposureSummary(
    exposure: Exposure,
    setScreenState: (AnchorScreenState) -> Unit,
    modifier: Modifier = Modifier
) {
    setScreenState(
        AnchorScreenState(
            topAppBarState = AnchorTopAppBarState(R.string.summary_top_bar_title)
        )
    )

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BasicSummarySection(
            exposure.updatedAt,
            exposure.title,
            exposure.description,
            modifier
        )
        exposure.preparation?.let {
            PreparationSummarySection(it, modifier)
        }
        exposure.review?.let {
            ReviewSummarySection(it, modifier)
        }
    }
}

@Composable
fun BasicSummarySection(
    updatedAt: Timestamp?,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    SummarySection(
        modifier = modifier,
        {
            updatedAt?.let {
                val completedAt = DateFormat.getDateInstance().format(it.toDate())
                TextSummaryItem(
                    label = R.string.summary_completed_at_label,
                    text = completedAt,
                    textStyle = MaterialTheme.typography.bodyMedium
                )
            }
        },
        { TextSummaryItem(text = title, textStyle = MaterialTheme.typography.titleLarge) },
        { TextSummaryItem(text = description) }
    )
}

@Composable
fun PreparationSummarySection(preparation: Preparation, modifier: Modifier = Modifier) {
    SummarySection(
        modifier = modifier,
        {
            LineSeparatedListSummaryItem(
                label = R.string.preparation_thoughts_label,
                list = preparation.thoughts
            )
        },
        {
            LineSeparatedListSummaryItem(
                label = R.string.preparation_interpretations_label,
                list = preparation.interpretations
            )
        },
        {
            LineSeparatedListSummaryItem(
                label = R.string.preparation_behaviors_label,
                list = preparation.behaviors
            )
        },
        {
            LineSeparatedListSummaryItem(
                label = R.string.preparation_actions_label,
                list = preparation.actions
            )
        }
    )
}

@Composable
fun ReviewSummarySection(review: Review, modifier: Modifier = Modifier) {
    val emotions =
        review.emotions.map {
            stringResource(
                when (it) {
                    Emotion.FEAR -> R.string.review_fear_chip
                    Emotion.SADNESS -> R.string.review_sadness_chip
                    Emotion.ANXIETY -> R.string.review_anxiety_chip
                    Emotion.GUILT -> R.string.review_guilt_chip
                    Emotion.SHAME -> R.string.review_shame_chip
                    Emotion.HAPPINESS -> R.string.review_happiness_chip
                }
            )
        }
    SummarySection(
        modifier = modifier,
        {
            CommaSeparatedListSummaryItem(
                label = R.string.review_emotions_label,
                list = emotions
            )
        },
        {
            LineSeparatedListSummaryItem(
                label = R.string.review_thoughts_label,
                list = review.thoughts
            )
        },
        {
            LineSeparatedListSummaryItem(
                label = R.string.review_sensations_label,
                list = review.sensations
            )
        },
        {
            LineSeparatedListSummaryItem(
                label = R.string.review_behaviors_label,
                list = review.behaviors
            )
        },
        { HorizontalDivider(Modifier.padding(8.dp)) },
        {
            RatingSummaryItem(
                label = R.string.review_experiencing_label,
                rating = review.experiencing
            )
        },
        {
            RatingSummaryItem(
                label = R.string.review_anchoring_label,
                rating = review.anchoring
            )
        },
        {
            RatingSummaryItem(
                label = R.string.review_thinking_label,
                rating = review.thinking
            )
        },
        {
            RatingSummaryItem(
                label = R.string.review_engaging_label,
                rating = review.engaging
            )
        },
        { HorizontalDivider(Modifier.padding(8.dp)) },
        {
            TextSummaryItem(
                label = R.string.review_learnings_label,
                text = review.learnings
            )
        }
    )
}
