package com.susieson.anchor.ui.exposure.summary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.susieson.anchor.R
import com.susieson.anchor.model.Emotion
import com.susieson.anchor.model.Review
import com.susieson.anchor.ui.components.BodyText
import com.susieson.anchor.ui.components.LabelText
import com.susieson.anchor.ui.components.LabeledItem
import com.susieson.anchor.ui.components.SameLineLabeledItem
import java.text.DateFormat
import kotlin.math.roundToInt

@Composable
fun ExposureSummaryScreen(
    viewModel: ExposureSummaryViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { ExposureSummaryTopBar(navController::navigateUp) },
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BasicSummarySection(
                viewModel.exposure.updatedAt,
                viewModel.exposure.title,
                viewModel.exposure.description,
                modifier = Modifier.fillMaxWidth()
            )
            viewModel.exposure.preparation?.let {
                PreparationSummarySection(
                    it.thoughts,
                    it.interpretations,
                    it.behaviors,
                    it.actions,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            viewModel.exposure.review?.let {
                ReviewSummarySection(it, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExposureSummaryTopBar(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.summary_top_bar_title)) },
        navigationIcon = {
            IconButton(onNavigateUp) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBack,
                    stringResource(R.string.content_description_back)
                )
            }
        },
        modifier = modifier
    )
}

@Composable
private fun BasicSummarySection(
    updatedAt: Timestamp?,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    OutlinedCard(modifier = modifier.padding(horizontal = 16.dp)) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            updatedAt?.let {
                val completedAt = DateFormat.getDateInstance().format(it.toDate())
                LabeledItem(
                    {
                        Text(
                            text = stringResource(R.string.summary_completed_at_label),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                ) {
                    Text(completedAt, style = MaterialTheme.typography.bodyMedium)
                }
            }
            Text(title, style = MaterialTheme.typography.titleLarge)
            Text(description, style = MaterialTheme.typography.bodyLarge)
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
                LabeledItem({ LabelText(stringResource(label)) }) {
                    item.forEach {
                        BodyText(it)
                    }
                }
            }
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
    LabeledItem({ LabelText(stringResource(R.string.review_emotions_label)) }) {
        val emotionsList = emotions.map { stringResource(it.label) }
        BodyText(emotionsList.joinToString(", "))
    }
    val items = listOf(
        thoughts to R.string.review_thoughts_label,
        sensations to R.string.review_sensations_label,
        behaviors to R.string.review_behaviors_label
    )
    items.forEach { (item, label) ->
        LabeledItem({ LabelText(stringResource(label)) }) {
            item.forEach {
                BodyText(it)
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
        SameLineLabeledItem({ LabelText(stringResource(label)) }) {
            BodyText(rating.roundToInt().toString())
        }
    }
}

@Composable
private fun ReviewLearningsSection(learnings: String) {
    LabeledItem({ LabelText(stringResource(R.string.review_learnings_label)) }) {
        BodyText(learnings)
    }
}

@Composable
private fun ReviewSummarySection(review: Review, modifier: Modifier = Modifier) {
    OutlinedCard(modifier = modifier.padding(horizontal = 16.dp)) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ReviewEmotionsSection(
                review.emotions,
                review.thoughts,
                review.sensations,
                review.behaviors
            )
            HorizontalDivider(Modifier.padding(8.dp))
            ReviewRatingsSection(
                review.experiencing,
                review.anchoring,
                review.thinking,
                review.engaging
            )
            HorizontalDivider(Modifier.padding(8.dp))
            ReviewLearningsSection(review.learnings)
        }
    }
}
