package com.susieson.anchor.ui.exposure.review

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.susieson.anchor.R
import com.susieson.anchor.ui.components.DiscardDialog
import com.susieson.anchor.ui.components.FormBackHandler
import com.susieson.anchor.ui.components.LabeledItem
import com.susieson.anchor.ui.components.LabeledItemWithSupporting
import com.susieson.anchor.ui.components.SliderWithLabel
import com.susieson.anchor.ui.components.TextFieldColumn
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ExposureReviewScreen(
    viewModel: ExposureReviewViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val emotions = remember { viewModel.emotions }
    val thoughts = remember { viewModel.thoughts }
    val sensations = remember { viewModel.sensations }
    val behaviors = remember { viewModel.behaviors }
    val experiencingRating = remember { viewModel.experiencingRating }
    val anchoringRating = remember { viewModel.anchoringRating }
    val thinkingRating = remember { viewModel.thinkingRating }
    val engagingRating = remember { viewModel.engagingRating }
    val learnings = remember { viewModel.learnings }

    val showDiscardDialog = remember { viewModel.showDiscardDialog }

    val isValid = remember { viewModel.isValid }
    val isEmpty = remember { viewModel.isEmpty }

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.review_top_bar_title)) },
                navigationIcon = {
                    IconButton(viewModel::onClose) {
                        Icon(
                            Icons.Default.Close,
                            stringResource(R.string.content_description_close)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = viewModel::addReview,
                        enabled = isValid
                    ) {
                        Icon(Icons.Default.Done, stringResource(R.string.content_description_done))
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val bringIntoViewRequester = remember { BringIntoViewRequester() }

            LabeledItem(
                label = {
                    Text(
                        stringResource(R.string.review_emotions_label),
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                content = {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        emotions.forEach { (emotion, selected) ->
                            FilterChip(
                                selected = selected,
                                onClick = { viewModel.onEmotionChanged(emotion) },
                                label = { Text(stringResource(emotion)) }
                            )
                        }
                    }
                }
            )
            LabeledItemWithSupporting(
                label = {
                    Text(
                        stringResource(R.string.review_thoughts_label),
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                supporting = {
                    Text(
                        stringResource(R.string.review_thoughts_body),
                        style = MaterialTheme.typography.bodySmall,
                        color = if (thoughts.isEmpty()) { MaterialTheme.colorScheme.error } else { MaterialTheme.colorScheme.onSurface }
                    )
                },
                content = {
                    TextFieldColumn(
                        texts = thoughts,
                        bringIntoViewRequester = bringIntoViewRequester,
                        onAdd = viewModel::onThoughtAdded,
                        onDelete = viewModel::onThoughtRemoved
                    )
                }
            )
            LabeledItemWithSupporting(
                label = {
                    Text(
                        stringResource(R.string.review_sensations_label),
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                supporting = {
                    Text(
                        stringResource(R.string.review_sensations_body),
                        style = MaterialTheme.typography.bodySmall,
                        color = if (sensations.isEmpty()) { MaterialTheme.colorScheme.error } else { MaterialTheme.colorScheme.onSurface }
                    )
                },
                content = {
                    TextFieldColumn(
                        texts = sensations,
                        bringIntoViewRequester = bringIntoViewRequester,
                        onAdd = viewModel::onSensationAdded,
                        onDelete = viewModel::onSensationRemoved
                    )
                }
            )
            LabeledItemWithSupporting(
                label = {
                    Text(
                        stringResource(R.string.review_behaviors_label),
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                supporting = {
                    Text(
                        stringResource(R.string.review_behaviors_body),
                        style = MaterialTheme.typography.bodySmall,
                        color = if (behaviors.isEmpty()) { MaterialTheme.colorScheme.error } else { MaterialTheme.colorScheme.onSurface }
                    )
                },
                content = {
                    TextFieldColumn(
                        texts = behaviors,
                        bringIntoViewRequester = bringIntoViewRequester,
                        onAdd = viewModel::onBehaviorAdded,
                        onDelete = viewModel::onBehaviorRemoved
                    )
                }
            )
            LabeledItem(
                label = {
                    Text(
                        text = stringResource(R.string.review_effectiveness_label),
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                content = {
                    Column(
                        modifier = modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        LabeledItem(
                            label = {
                                Text(
                                    stringResource(R.string.review_experiencing_label),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (experiencingRating == 0f) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                                )
                            },
                            content = {
                                SliderWithLabel(
                                    value = experiencingRating,
                                    onValueChange = viewModel::onExperiencingRatingChanged,
                                    valueRange = 0f..10f,
                                    steps = 9,
                                )
                            }
                        )
                        LabeledItem(
                            label = {
                                Text(
                                    stringResource(R.string.review_anchoring_label),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (anchoringRating == 0f) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                                )
                            },
                            content = {
                                SliderWithLabel(
                                    value = anchoringRating,
                                    onValueChange = viewModel::onAnchoringRatingChanged,
                                    valueRange = 0f..10f,
                                    steps = 9,
                                )
                            }
                        )
                        LabeledItem(
                            label = {
                                Text(
                                    stringResource(R.string.review_thinking_label),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (thinkingRating == 0f) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                                )
                            },
                            content = {
                                SliderWithLabel(
                                    value = thinkingRating,
                                    onValueChange = viewModel::onThinkingRatingChanged,
                                    valueRange = 0f..10f,
                                    steps = 9,
                                )
                            }
                        )
                        LabeledItem(
                            label = {
                                Text(
                                    stringResource(R.string.review_engaging_label),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (engagingRating == 0f) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                                )
                            },
                            content = {
                                SliderWithLabel(
                                    value = engagingRating,
                                    onValueChange = viewModel::onEngagingRatingChanged,
                                    valueRange = 0f..10f,
                                    steps = 9,
                                )
                            }
                        )
                    }
                }
            )
            LabeledItemWithSupporting(
                label = {
                    Text(
                        stringResource(R.string.review_learnings_label),
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                supporting = {
                    Text(
                        stringResource(R.string.review_learnings_body),
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                content = {
                    OutlinedTextField(
                        value = learnings,
                        isError = learnings.isBlank(),
                        label = {},
                        placeholder = {},
                        supportingText = {
                            if (learnings.isBlank()) Text(
                                stringResource(R.string.review_learnings_error),
                                color = MaterialTheme.colorScheme.error
                            )
                        },
                        onValueChange = viewModel::onLearningsChange,
                        modifier = modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .bringIntoViewRequester(bringIntoViewRequester)
                            .onFocusChanged {
                                if (it.isFocused) {
                                    coroutineScope.launch {
                                        bringIntoViewRequester.bringIntoView()
                                    }
                                }
                            }
                            .focusTarget(),
                        singleLine = false,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                    )
                }
            )
        }
    }
    DiscardDialog(
        show = showDiscardDialog,
        onDiscard = navController::navigateUp,
        onSetShow = viewModel::onShowDiscardDialogChange
    )
    FormBackHandler(
        isEmpty = isEmpty,
        onDiscard = navController::navigateUp,
        onShowDiscardDialog = { viewModel.onShowDiscardDialogChange(true) }
    )
}
