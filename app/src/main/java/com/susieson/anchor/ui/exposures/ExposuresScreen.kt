package com.susieson.anchor.ui.exposures

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.susieson.anchor.R
import com.susieson.anchor.TopAppBarState
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.model.Status
import com.susieson.anchor.ui.components.LoadingScreen
import kotlinx.datetime.toKotlinInstant
import nl.jacobras.humanreadable.HumanReadable
import java.text.DateFormat

@Composable
fun ExposuresScreen(
    modifier: Modifier = Modifier,
    userId: String,
    onStart: (String) -> Unit,
    onItemClick: (String, String) -> Unit,
    onTopAppBarStateChanged: (TopAppBarState?) -> Unit,
    exposuresViewModel: ExposuresViewModel = hiltViewModel()
) {
    onTopAppBarStateChanged(
        TopAppBarState(
            title = R.string.anchor_top_bar_title,
            onAction = { onStart(userId) }
        )
    )

    val exposuresState by exposuresViewModel.exposures.collectAsState()

    val exposures = exposuresState?.filterNot { it.status == Status.DRAFT }

    if (exposures == null) {
        LoadingScreen(modifier = modifier.fillMaxSize())
    } else if (exposures.isEmpty()) {
        EmptyExposureList(
            modifier = modifier.fillMaxSize()
        )
    } else {
        ExposureList(
            modifier = modifier.fillMaxSize(),
            exposures = exposures,
            onItemClick = { exposureId -> onItemClick(userId, exposureId) }
        )
    }

    LaunchedEffect(userId) {
        exposuresViewModel.get(userId)
    }
}

@Composable
fun EmptyExposureList(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.illustration_sailboat),
            modifier = Modifier.padding(0.dp, 24.dp),
            contentDescription = null
        )
        Text(stringResource(R.string.exposures_title), style = MaterialTheme.typography.titleLarge)
        Text(stringResource(R.string.exposures_body), style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun ExposureList(
    modifier: Modifier = Modifier,
    exposures: List<Exposure>,
    onItemClick: (String) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(exposures.size) { index ->
            ListItem(
                overlineContent = {
                    val timestamp = exposures[index].updatedAt
                    timestamp?.let {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            return@let HumanReadable.timeAgo(it.toInstant().toKotlinInstant())
                        } else {
                            return@let DateFormat.getDateTimeInstance().format(it.toDate())
                        }
                    }?.let {
                        Text(
                            stringResource(
                                when (exposures[index].status) {
                                    Status.COMPLETED -> R.string.exposure_completed_date
                                    Status.IN_PROGRESS -> R.string.exposure_in_progress_date
                                    Status.READY -> R.string.exposure_ready_date
                                    Status.DRAFT -> R.string.exposure_draft_date
                                },
                                it
                            )
                        )
                    }
                },
                headlineContent = { Text(exposures[index].title.ifBlank { stringResource(R.string.exposures_no_title) }) },
                supportingContent = { Text(exposures[index].description.ifBlank { stringResource(R.string.exposures_no_description) }) },
                modifier = Modifier.clickable { onItemClick(exposures[index].id) }
            )
            HorizontalDivider()
        }
    }
}