package com.susieson.anchor.ui.exposure.summary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.susieson.anchor.R
import com.susieson.anchor.model.Emotion
import com.susieson.anchor.ui.components.LabeledItemColumn
import com.susieson.anchor.ui.components.LabeledItemRow
import java.text.DateFormat
import kotlin.math.roundToInt

@Suppress("LongParameterList")
@Composable
fun ExposureSummaryContent(
    updatedAt: Timestamp,
    title: String,
    description: String,
    prepThoughts: List<String>,
    prepInterpretations: List<String>,
    prepBehaviors: List<String>,
    prepActions: List<String>,
    emotions: List<Emotion>,
    revThoughts: List<String>,
    revSensations: List<String>,
    revBehaviors: List<String>,
    experiencing: Float,
    anchoring: Float,
    thinking: Float,
    engaging: Float,
    learnings: String,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(innerPadding)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BasicSummarySection(
            updatedAt,
            title,
            description,
            modifier = Modifier.fillMaxWidth()
        )
        PreparationSummarySection(
            thoughts = prepThoughts,
            interpretations = prepInterpretations,
            behaviors = prepBehaviors,
            actions = prepActions,
            modifier = Modifier.fillMaxWidth()
        )
        ReviewSummarySection(
            emotions = emotions,
            thoughts = revThoughts,
            sensations = revSensations,
            behaviors = revBehaviors,
            experiencing = experiencing,
            anchoring = anchoring,
            thinking = thinking,
            engaging = engaging,
            learnings = learnings,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun BasicSummarySection(
    updatedAt: Timestamp,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    OutlinedCard(modifier = modifier.padding(horizontal = 16.dp)) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val completedAt = DateFormat.getDateInstance().format(updatedAt.toDate())
            LabeledItemColumn({ Text(stringResource(R.string.summary_completed_at_label)) }) {
                Text(completedAt, style = MaterialTheme.typography.bodySmall)
            }
            Text(title, style = MaterialTheme.typography.titleLarge)
            Text(description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun PreparationSummarySection(
    thoughts: List<String>,
    interpretations: List<String>,
    behaviors: List<String>,
    actions: List<String>,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        thoughts to R.string.preparation_thoughts_label,
        interpretations to R.string.preparation_interpretations_label,
        behaviors to R.string.preparation_behaviors_label,
        actions to R.string.preparation_actions_label
    )
    OutlinedCard(modifier = modifier.padding(horizontal = 16.dp)) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items.forEach { (item, label) ->
                LabeledItemColumn({ Text(stringResource(label)) }) {
                    item.forEach {
                        Text(it, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
private fun ReviewSummarySection(
    emotions: List<Emotion>,
    thoughts: List<String>,
    sensations: List<String>,
    behaviors: List<String>,
    experiencing: Float,
    anchoring: Float,
    thinking: Float,
    engaging: Float,
    learnings: String,
    modifier: Modifier = Modifier
) {
    OutlinedCard(modifier = modifier.padding(horizontal = 16.dp)) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ReviewEmotionsSection(
                emotions,
                thoughts,
                sensations,
                behaviors
            )
            HorizontalDivider(Modifier.padding(8.dp))
            ReviewRatingsSection(
                experiencing,
                anchoring,
                thinking,
                engaging
            )
            HorizontalDivider(Modifier.padding(8.dp))
            ReviewLearningsSection(learnings)
        }
    }
}

@Composable
private fun ReviewEmotionsSection(
    emotions: List<Emotion>,
    thoughts: List<String>,
    sensations: List<String>,
    behaviors: List<String>
) {
    LabeledItemColumn({ Text(stringResource(R.string.review_emotions_label)) }) {
        val emotionsList = emotions.map { stringResource(it.label) }
        Text(emotionsList.joinToString(", "), style = MaterialTheme.typography.bodyMedium)
    }
    val items = listOf(
        thoughts to R.string.review_thoughts_label,
        sensations to R.string.review_sensations_label,
        behaviors to R.string.review_behaviors_label
    )
    items.forEach { (item, label) ->
        LabeledItemColumn({ Text(stringResource(label)) }) {
            item.forEach {
                Text(it, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun ReviewRatingsSection(
    experiencing: Float,
    anchoring: Float,
    thinking: Float,
    engaging: Float
) {
    val ratings = listOf(
        experiencing to R.string.review_experiencing_label,
        anchoring to R.string.review_anchoring_label,
        thinking to R.string.review_thinking_label,
        engaging to R.string.review_engaging_label
    )
    ratings.forEach { (rating, label) ->
        LabeledItemRow({ Text(stringResource(label)) }) {
            Text(rating.roundToInt().toString(), style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun ReviewLearningsSection(learnings: String) {
    LabeledItemColumn({ Text(stringResource(R.string.review_learnings_label)) }) {
        Text(learnings, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview
@Composable
private fun ExposureSummaryContentPreview() {
    ExposureSummaryContent(
        updatedAt = Timestamp.now(),
        title = "Title",
        description = "Description",
        prepThoughts = listOf("Thought 1", "Thought 2"),
        prepInterpretations = listOf("Interpretation 1", "Interpretation 2"),
        prepBehaviors = listOf("Behavior 1", "Behavior 2"),
        prepActions = listOf("Action 1", "Action 2"),
        emotions = listOf(Emotion.FEAR, Emotion.SADNESS),
        revThoughts = listOf("Thought 1", "Thought 2"),
        revSensations = listOf("Sensation 1", "Sensation 2"),
        revBehaviors = listOf("Behavior 1", "Behavior 2"),
        experiencing = 5f,
        anchoring = 5f,
        thinking = 5f,
        engaging = 5f,
        learnings = "Learnings",
        innerPadding = PaddingValues()
    )
}
