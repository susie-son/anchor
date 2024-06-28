package com.susieson.anchor.ui.exposures

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.susieson.anchor.ExposurePreparation
import com.susieson.anchor.ExposureReady
import com.susieson.anchor.ExposureReview
import com.susieson.anchor.ExposureSummary
import com.susieson.anchor.R
import com.susieson.anchor.Settings
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.model.Status
import com.susieson.anchor.ui.components.Loading
import kotlinx.datetime.toKotlinInstant
import nl.jacobras.humanreadable.HumanReadable
import java.text.DateFormat

@Composable
fun ExposuresScreen(
    viewModel: ExposuresViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val exposures = viewModel.exposures.collectAsState(null).value

    Scaffold(
        topBar = { ExposuresTopBar { navController.navigate(Settings(viewModel.userId)) } },
        floatingActionButton = {
            ExposuresFloatingActionButton {
                navController.navigate(ExposurePreparation(viewModel.userId))
            }
        },
        modifier = modifier,
    ) { innerPadding ->
        when (exposures) {
            null -> Loading(modifier = Modifier.fillMaxSize().padding(innerPadding))
            else -> ExposuresContent(
                exposures = exposures,
                onItemClick = { exposure ->
                    navController.navigate(
                        when (exposure.status) {
                            Status.DRAFT -> ExposurePreparation(viewModel.userId)
                            Status.READY -> ExposureReady(viewModel.userId, exposure)
                            Status.IN_PROGRESS -> ExposureReview(viewModel.userId, exposure)
                            Status.COMPLETED -> ExposureSummary(exposure)
                        }
                    )
                },
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExposuresTopBar(
    modifier: Modifier = Modifier,
    onNavigateSettings: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.app_name)) },
        actions = {
            IconButton(onNavigateSettings) {
                Icon(Icons.Default.Settings, stringResource(R.string.content_description_settings))
            }
        },
        modifier = modifier
    )
}

@Composable
private fun ExposuresFloatingActionButton(
    modifier: Modifier = Modifier,
    onNavigateExposure: () -> Unit
) {
    ExtendedFloatingActionButton(onNavigateExposure, modifier = modifier) {
        Icon(Icons.Default.Add, null)
        Text(stringResource(R.string.exposures_start_button), modifier = Modifier.padding(start = 8.dp))
    }
}

@Composable
private fun ExposuresContent(
    exposures: List<Exposure>,
    onItemClick: (Exposure) -> Unit,
    modifier: Modifier = Modifier
) {
    if (exposures.isEmpty()) {
        EmptyExposureList(modifier)
    } else {
        ExposureList(exposures, onItemClick, modifier)
    }
}

@Composable
private fun EmptyExposureList(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.illustration_sailboat),
            modifier = Modifier.padding(vertical = 24.dp),
            contentDescription = null
        )
        Text(stringResource(R.string.exposures_title), style = MaterialTheme.typography.titleLarge)
        Text(stringResource(R.string.exposures_body), style = MaterialTheme.typography.bodyLarge)
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyExposureListPreview() {
    EmptyExposureList()
}

@Composable
private fun ExposureList(
    exposures: List<Exposure>,
    onItemClick: (Exposure) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier) {
        items(exposures) { exposure ->
            val formattedTime by remember {derivedStateOf {
                exposure.updatedAt?.let { timestamp ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        HumanReadable.timeAgo(timestamp.toInstant().toKotlinInstant())
                    } else {
                        DateFormat.getDateTimeInstance().format(timestamp.toDate())
                    }
                }
            }}

            ListItem(
                overlineContent = {
                    formattedTime?.let { StatusTextWithTimestamp(it, exposure.status) }
                },
                headlineContent = { Text(exposure.title.ifBlank { stringResource(R.string.exposures_no_title) }) },
                supportingContent = {
                    Text(exposure.description.ifBlank { stringResource(R.string.exposures_no_description) })
                },
                modifier = Modifier.clickable { onItemClick(exposure) }
            )
            HorizontalDivider()
        }
    }
}

@Composable
private fun StatusTextWithTimestamp(
    time: String,
    status: Status,
    modifier: Modifier = Modifier
) {
    val statusText =
        stringResource(
            when (status) {
                Status.COMPLETED -> R.string.exposure_completed_date
                Status.IN_PROGRESS -> R.string.exposure_in_progress_date
                Status.READY -> R.string.exposure_ready_date
                Status.DRAFT -> R.string.exposure_draft_date
            },
            time
        )
    Text(statusText, style = MaterialTheme.typography.bodySmall, modifier = modifier)
}

@Preview
@Composable
private fun ExposureListPreview() {
    ExposureList(
        exposures = listOf(
            Exposure(
                title = "Title 1",
                description = "Description 1",
                status = Status.DRAFT,
                updatedAt = Timestamp(seconds = 1719603129, nanoseconds = 0)
            ),
            Exposure(
                title = "Title 2",
                description = "Description 2",
                status = Status.READY,
                updatedAt = Timestamp(seconds = 1719603129, nanoseconds = 0)
            ),
            Exposure(
                title = "Title 3",
                description = "Description 3",
                status = Status.IN_PROGRESS,
                updatedAt = Timestamp(seconds = 1719603129, nanoseconds = 0)
            ),
            Exposure(
                title = "Title 4",
                description = "Description 4",
                status = Status.COMPLETED,
                updatedAt = Timestamp(seconds = 1719603129, nanoseconds = 0)
            )
        ),
        onItemClick = {}
    )
}
