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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.susieson.anchor.R
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.model.Status
import com.susieson.anchor.ui.AnchorScreenState
import com.susieson.anchor.ui.components.AnchorFabState
import com.susieson.anchor.ui.components.AnchorTopAppBarState
import com.susieson.anchor.ui.components.Loading
import java.text.DateFormat
import kotlinx.coroutines.delay
import kotlinx.datetime.toKotlinInstant
import nl.jacobras.humanreadable.HumanReadable

@Composable
fun ExposuresScreen(
    userId: String,
    modifier: Modifier = Modifier,
    onItemSelect: (String) -> Unit,
    setScreenState: (AnchorScreenState) -> Unit,
    viewModel: ExposuresViewModel = hiltViewModel()
) {
    val exposureId by viewModel.exposureId.collectAsState()
    val exposures by viewModel.exposures.collectAsState()

    setScreenState(
        AnchorScreenState(
            topAppBarState = AnchorTopAppBarState.Default,
            fabState = AnchorFabState(
                text = R.string.exposures_start_button,
                icon = Icons.Default.Add,
                onClick = { viewModel.addExposure(userId) }
            )
        )
    )

    when {
        exposures == null -> Loading(modifier = modifier)
        exposures!!.isNotEmpty() -> ExposureList(
            modifier = modifier,
            exposures = exposures!!,
            onItemClick = onItemSelect
        )
        else -> EmptyExposureList(modifier = modifier)
    }

    LaunchedEffect(userId) {
        viewModel.getExposureList(userId)
    }

    DisposableEffect(exposureId) {
        onDispose {
            viewModel.resetExposureId()
        }
    }

    exposureId?.let {
        onItemSelect(it)
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
        items(exposures) { exposure ->
            val timestamp = exposure.updatedAt

            val time by produceState<String?>(initialValue = null, exposure) {
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
                    delay(60_000)
                }
            }

            ListItem(
                overlineContent = {
                    time?.let {
                        StatusWithTimestamp(
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
                            stringResource(
                                R.string.exposures_no_description
                            )
                        }
                    )
                },
                modifier = modifier.clickable { onItemClick(exposure.id) }
            )
            HorizontalDivider(modifier = modifier)
        }
    }
}

@Composable
fun StatusWithTimestamp(time: String, status: Status, modifier: Modifier = Modifier) {
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
