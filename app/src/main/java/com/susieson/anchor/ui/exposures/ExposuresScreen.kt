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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
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
import com.susieson.anchor.R
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
    onItemSelect: (Exposure) -> Unit,
    viewModel: ExposuresViewModel,
    modifier: Modifier = Modifier
) {
    val exposuresState by viewModel.exposures.collectAsState()
    val exposures = exposuresState

    when (exposures?.isEmpty()) {
        null -> Loading(modifier = modifier.fillMaxSize())
        false -> ExposureList(
            exposures = exposures,
            onItemClick = onItemSelect,
            modifier = modifier.fillMaxSize(),
        )
        true -> EmptyExposureList(modifier = modifier.fillMaxSize())
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
    exposures: List<Exposure>,
    modifier: Modifier = Modifier,
    onItemClick: (Exposure) -> Unit
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
                    delay(TimeReloadInterval)
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
                modifier = Modifier.clickable { onItemClick(exposure) }
            )
            HorizontalDivider()
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
