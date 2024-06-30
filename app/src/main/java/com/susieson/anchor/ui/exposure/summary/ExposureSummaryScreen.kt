package com.susieson.anchor.ui.exposure.summary

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ExposureSummaryScreen(
    viewModel: ExposureSummaryViewModel,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val exposure = viewModel.exposure
    Scaffold(
        topBar = { ExposureSummaryTopBar(onNavigateUp) },
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        ExposureSummaryContent(
            updatedAt = exposure.updatedAt,
            title = exposure.title,
            description = exposure.description,
            prepThoughts = exposure.preparation.thoughts,
            prepInterpretations = exposure.preparation.interpretations,
            prepBehaviors = exposure.preparation.behaviors,
            prepActions = exposure.preparation.actions,
            emotions = exposure.review.emotions,
            revThoughts = exposure.review.thoughts,
            revSensations = exposure.review.sensations,
            revBehaviors = exposure.review.behaviors,
            experiencing = exposure.review.experiencing,
            anchoring = exposure.review.anchoring,
            thinking = exposure.review.thinking,
            engaging = exposure.review.engaging,
            learnings = exposure.review.learnings,
            innerPadding = innerPadding
        )
    }
}
