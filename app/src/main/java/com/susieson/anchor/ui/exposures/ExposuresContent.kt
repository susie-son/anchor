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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.susieson.anchor.R
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.model.Status
import kotlinx.datetime.toKotlinInstant
import nl.jacobras.humanreadable.HumanReadable
import java.text.DateFormat

@Composable
fun ExposuresContent(
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
        modifier = modifier.fillMaxSize()
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
            val timeAgo by remember {
                derivedStateOf {
                    exposure.updatedAt.let {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            HumanReadable.timeAgo(it.toInstant().toKotlinInstant())
                        } else {
                            DateFormat.getDateTimeInstance().format(it.toDate())
                        }
                    }
                }
            }

            ListItem(
                overlineContent = {
                    StatusTextWithTimestamp(timeAgo, exposure.status)
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
    timeAgo: String,
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
            timeAgo
        )
    Text(statusText, style = MaterialTheme.typography.labelSmall, modifier = modifier)
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
