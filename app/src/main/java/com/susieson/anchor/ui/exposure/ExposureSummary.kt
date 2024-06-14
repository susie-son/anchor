package com.susieson.anchor.ui.exposure

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.susieson.anchor.R
import com.susieson.anchor.TopAppBarState
import com.susieson.anchor.model.Emotion
import com.susieson.anchor.model.Exposure
import java.text.DateFormat
import kotlin.math.roundToInt

@Composable
fun ExposureSummary(
    exposure: Exposure,
    onBack: () -> Unit,
    setTopAppBarState: (TopAppBarState) -> Unit,
    modifier: Modifier = Modifier,
) {
    setTopAppBarState(
        TopAppBarState(
            title = R.string.summary_top_bar_title,
            onBack = onBack
        )
    )

    val preparation = exposure.preparation
    val review = exposure.review

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        exposure.updatedAt?.let {
            Text(
                stringResource(R.string.summary_completed_at_label),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                DateFormat.getDateInstance().format(it.toDate()),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            exposure.title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(exposure.description, style = MaterialTheme.typography.bodyLarge)

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        preparation?.let { preparation ->
            val thoughts = preparation.thoughts
            val interpretations = preparation.interpretations
            val behaviors = preparation.behaviors
            val actions = preparation.actions

            Text(
                stringResource(R.string.preparation_thoughts_label),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            thoughts.forEach {
                Text(it, style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                stringResource(R.string.preparation_interpretations_label),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            interpretations.forEach {
                Text(it, style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                stringResource(R.string.preparation_behaviors_label),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            behaviors.forEach {
                Text(it, style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                stringResource(R.string.preparation_actions_label),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            actions.forEach {
                Text(it, style = MaterialTheme.typography.bodyLarge)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
        }

        review?.let { review ->
            val emotions = review.emotions.map {
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
            val thoughts = review.thoughts
            val sensations = review.sensations
            val behaviors = review.behaviors
            val experiencing = review.experiencing
            val anchoring = review.anchoring
            val thinking = review.thinking
            val engaging = review.engaging
            val learnings = review.learnings

            Text(
                stringResource(R.string.review_emotions_label),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                emotions.joinToString(", "),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                stringResource(R.string.review_thoughts_label),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            thoughts.forEach {
                Text(it, style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                stringResource(R.string.review_sensations_label),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            sensations.forEach {
                Text(it, style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                stringResource(R.string.review_behaviors_label),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            behaviors.forEach {
                Text(it, style = MaterialTheme.typography.bodyLarge)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    stringResource(R.string.review_experiencing_label),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    experiencing.roundToInt().toString(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    stringResource(R.string.review_anchoring_label),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    anchoring.roundToInt().toString(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    stringResource(R.string.review_thinking_label),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    thinking.roundToInt().toString(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    stringResource(R.string.review_engaging_label),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    engaging.roundToInt().toString(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            Text(
                stringResource(R.string.review_learnings_label),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(learnings, style = MaterialTheme.typography.bodyLarge)
        }
    }
}