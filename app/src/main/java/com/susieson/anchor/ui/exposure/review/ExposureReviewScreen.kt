package com.susieson.anchor.ui.exposure.review

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.susieson.anchor.model.Exposure
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
    val emotion by remember { viewModel.emotionState }
    val rating by remember { viewModel.ratingState }
    val learnings by remember { viewModel.learningsState }

    val emotionsListener by remember { viewModel.emotionsListener }
    val ratingsListener by remember { viewModel.ratingsListener }
    val learningsListener by remember { viewModel.learningsListener }

    val showDiscardDialog by remember { viewModel.showDiscardDialog }

    val isEmpty by remember { viewModel.isEmpty }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        val bringIntoViewRequester = remember { BringIntoViewRequester() }
        EmotionFormSection(emotion, emotionsListener, bringIntoViewRequester)
        RatingFormSection(rating, ratingsListener)
        LearningsFormSection(learnings, learningsListener, bringIntoViewRequester)
    }
    DiscardDialog(
        show = showDiscardDialog,
        onDiscard = onNavigateUp,
        onSetShow = viewModel::setShowDiscardDialog
    )
    FormBackHandler(
        isEmpty = isEmpty,
        onDiscard = onNavigateUp,
        onShowDiscardDialog = { viewModel.setShowDiscardDialog(true) }
    )
}
