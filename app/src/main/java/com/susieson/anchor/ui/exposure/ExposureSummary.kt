package com.susieson.anchor.ui.exposure

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.susieson.anchor.ui.components.AnchorIconButton
import com.susieson.anchor.ui.components.AnchorScaffold
import com.susieson.anchor.ui.components.AnchorTopAppBar
import com.susieson.anchor.ui.components.CommaSeparatedListSummaryItem
import com.susieson.anchor.ui.components.LineSeparatedListSummaryItem
import com.susieson.anchor.ui.components.RatingSummaryItem
import com.susieson.anchor.ui.components.SummarySection
import com.susieson.anchor.ui.components.TextSummaryItem
import java.text.DateFormat

@Composable
fun ExposureSummary(
    exposure: Exposure,
    onNavigateUp: () -> Unit,
    setScaffold: (AnchorScaffold) -> Unit,
    modifier: Modifier = Modifier
) {
    setScaffold(
        AnchorScaffold(
            topAppBar = AnchorTopAppBar(
                title = R.string.summary_top_bar_title,
                navigationIcon = AnchorIconButton(
                    onClick = onNavigateUp,
                    icon = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = R.string.content_description_back
                )
            )
        )
    )

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BasicSummarySection(
            exposure.updatedAt,
            exposure.title,
            exposure.description,
            modifier = Modifier.fillMaxWidth()
        )
        exposure.preparation?.let {
            PreparationSummarySection(it, modifier = Modifier.fillMaxWidth())
        }
        exposure.review?.let {
            ReviewSummarySection(it, modifier = Modifier.fillMaxWidth())
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
        items = listOf(
            {
                updatedAt?.let {
                    val completedAt = DateFormat.getDateInstance().format(it.toDate())
                    TextSummaryItem(
                        completedAt,
                        label = R.string.summary_completed_at_label,
                        textStyle = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            {
                TextSummaryItem(
                    title,
                    label = null,
                    textStyle = MaterialTheme.typography.titleLarge
                )
            },
            { TextSummaryItem(description, label = null) }
        )
    )
}

@Composable
fun PreparationSummarySection(preparation: Preparation, modifier: Modifier = Modifier) {
    SummarySection(
        modifier = modifier,
        items = listOf(
            {
                LineSeparatedListSummaryItem(
                    preparation.thoughts,
                    label = R.string.preparation_thoughts_label
                )
            },
            {
                LineSeparatedListSummaryItem(
                    preparation.interpretations,
                    label = R.string.preparation_interpretations_label
                )
            },
            {
                LineSeparatedListSummaryItem(
                    preparation.behaviors,
                    label = R.string.preparation_behaviors_label
                )
            },
            {
                LineSeparatedListSummaryItem(
                    preparation.actions,
                    label = R.string.preparation_actions_label
                )
            }
        )
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
        items = listOf(
            {
                CommaSeparatedListSummaryItem(emotions, label = R.string.review_emotions_label)
            },
            {
                LineSeparatedListSummaryItem(
                    review.thoughts,
                    label = R.string.review_thoughts_label
                )
            },
            {
                LineSeparatedListSummaryItem(
                    review.sensations,
                    label = R.string.review_sensations_label
                )
            },
            {
                LineSeparatedListSummaryItem(
                    review.behaviors,
                    label = R.string.review_behaviors_label
                )
            },
            { HorizontalDivider(Modifier.padding(8.dp)) },
            {
                RatingSummaryItem(review.experiencing, label = R.string.review_experiencing_label)
            },
            {
                RatingSummaryItem(review.anchoring, label = R.string.review_anchoring_label)
            },
            {
                RatingSummaryItem(review.thinking, label = R.string.review_thinking_label)
            },
            {
                RatingSummaryItem(review.engaging, label = R.string.review_engaging_label)
            },
            { HorizontalDivider(Modifier.padding(8.dp)) },
            {
                TextSummaryItem(review.learnings, label = R.string.review_learnings_label)
            }
        )
    )
}
