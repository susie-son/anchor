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
import com.susieson.anchor.model.Preparation
import com.susieson.anchor.model.Review
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
            modifier = Modifier.padding(innerPadding).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BasicSummarySection(
                viewModel.exposure.updatedAt,
                viewModel.exposure.title,
                viewModel.exposure.description,
                modifier = Modifier.fillMaxWidth()
            )
            viewModel.exposure.preparation?.let {
                PreparationSummarySection(it, modifier = Modifier.fillMaxWidth())
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
                    label = {
                        Text(
                            stringResource(R.string.summary_completed_at_label),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    content = {
                        Text(completedAt, style = MaterialTheme.typography.bodyMedium)
                    }
                )
            }
            Text(title, style = MaterialTheme.typography.titleLarge)
            Text(description, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun PreparationSummarySection(preparation: Preparation, modifier: Modifier = Modifier) {
    OutlinedCard(modifier = modifier.padding(horizontal = 16.dp)) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LabeledItem(
                label = {
                    Text(
                        stringResource(R.string.preparation_thoughts_label),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                content = {
                    preparation.thoughts.forEach {
                        Text(it, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            )
            LabeledItem(
                label = {
                    Text(
                        stringResource(R.string.preparation_interpretations_label),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                content = {
                    preparation.interpretations.forEach {
                        Text(it, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            )
            LabeledItem(
                label = {
                    Text(
                        stringResource(R.string.preparation_behaviors_label),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                content = {
                    preparation.behaviors.forEach {
                        Text(it, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            )
            LabeledItem(
                label = {
                    Text(
                        stringResource(R.string.preparation_actions_label),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                content = {
                    preparation.actions.forEach {
                        Text(it, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            )
        }
    }
}

@Composable
private fun ReviewSummarySection(review: Review, modifier: Modifier = Modifier) {
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

    OutlinedCard(modifier = modifier.padding(horizontal = 16.dp)) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LabeledItem(
                label = {
                    Text(
                        stringResource(R.string.review_emotions_label),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                content = {
                    Text(
                        emotions.joinToString(", "),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            )
            LabeledItem(
                label = {
                    Text(
                        stringResource(R.string.review_thoughts_label),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                content = {
                    review.thoughts.forEach {
                        Text(it, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            )
            LabeledItem(
                label = {
                    Text(
                        stringResource(R.string.review_sensations_label),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                content = {
                    review.sensations.forEach {
                        Text(it, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            )
            LabeledItem(
                label = {
                    Text(
                        stringResource(R.string.review_behaviors_label),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                content = {
                    review.behaviors.forEach {
                        Text(it, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            )
            HorizontalDivider(Modifier.padding(8.dp))
            SameLineLabeledItem(
                label = {
                    Text(
                        stringResource(R.string.review_experiencing_label),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                content = {
                    Text(
                        review.experiencing.roundToInt().toString(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            )
            SameLineLabeledItem(
                label = {
                    Text(
                        stringResource(R.string.review_anchoring_label),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                content = {
                    Text(
                        review.anchoring.roundToInt().toString(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            )
            SameLineLabeledItem(
                label = {
                    Text(
                        stringResource(R.string.review_thinking_label),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                content = {
                    Text(
                        review.thinking.roundToInt().toString(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            )
            SameLineLabeledItem(
                label = {
                    Text(
                        stringResource(R.string.review_engaging_label),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                content = {
                    Text(
                        review.engaging.roundToInt().toString(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            )
            HorizontalDivider(Modifier.padding(8.dp))
            LabeledItem(
                label = {
                    Text(
                        stringResource(R.string.review_learnings_label),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                content = {
                    Text(review.learnings, style = MaterialTheme.typography.bodyLarge)
                }
            )
        }
    }
}
