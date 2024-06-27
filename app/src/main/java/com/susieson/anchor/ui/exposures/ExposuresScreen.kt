package com.susieson.anchor.ui.exposures

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.susieson.anchor.ExposurePreparation
import com.susieson.anchor.ExposureReady
import com.susieson.anchor.ExposureReview
import com.susieson.anchor.ExposureSummary
import com.susieson.anchor.R
import com.susieson.anchor.Settings
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.model.Status
import com.susieson.anchor.ui.components.Loading
import kotlinx.coroutines.delay
import kotlinx.datetime.toKotlinInstant
import nl.jacobras.humanreadable.HumanReadable
import java.text.DateFormat

const val TimeReloadInterval = 60_000L

@Composable
fun ExposuresScreen(
    viewModel: ExposuresViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val exposures = viewModel.exposures.collectAsState(null).value

    Scaffold(
        topBar = {
            ExposuresTopBar {
                navController.navigate(Settings(viewModel.userId))
            }
        },
        floatingActionButton = {
            ExposuresFloatingActionButton {
                navController.navigate(ExposurePreparation(viewModel.userId))
            }
        },
        modifier = modifier,
    ) { innerPadding ->
        when (exposures) {
            null -> Loading(modifier = Modifier.padding(innerPadding))
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
                modifier = Modifier.padding(innerPadding)
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
                Icon(
                    Icons.Default.Settings,
                    stringResource(R.string.content_description_settings)
                )
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
    ExtendedFloatingActionButton(
        onClick = onNavigateExposure,
        modifier = modifier
    ) {
        Icon(Icons.Default.Add, null)
        Text(stringResource(R.string.exposures_start_button))
    }
}

@Composable
private fun ExposuresContent(
    exposures: List<Exposure>,
    onItemClick: (Exposure) -> Unit,
    modifier: Modifier = Modifier
) {
    when (exposures.isEmpty()) {
        true -> EmptyExposureList(modifier = modifier)
        false -> ExposureList(
            exposures = exposures,
            onItemClick = onItemClick,
            modifier = modifier
        )
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
            modifier = Modifier.padding(0.dp, 24.dp),
            contentDescription = null
        )
        Text(stringResource(R.string.exposures_title), style = MaterialTheme.typography.titleLarge)
        Text(stringResource(R.string.exposures_body), style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun ExposureList(
    exposures: List<Exposure>,
    modifier: Modifier = Modifier,
    onItemClick: (Exposure) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(exposures) { exposure ->
            val timestamp = exposure.updatedAt
            val time by produceState<String?>(null, exposure) {
                while (true) {
                    if (timestamp == null) {
                        value = null
                    } else {
                        val formattedTime =
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                HumanReadable.timeAgo(timestamp.toInstant().toKotlinInstant())
                            } else {
                                DateFormat.getDateTimeInstance().format(timestamp.toDate())
                            }
                        value = formattedTime
                    }
                    delay(TimeReloadInterval)
                }
            }

            ListItem(
                overlineContent = {
                    time?.let {
                        StatusTextWithTimestamp(
                            time = it,
                            status = exposure.status
                        )
                    }
                },
                headlineContent = {
                    Text(
                        exposure.title.ifBlank { stringResource(R.string.exposures_no_title) }
                    )
                },
                supportingContent = {
                    Text(
                        exposure.description.ifBlank {
                            stringResource(R.string.exposures_no_description)
                        }
                    )
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
