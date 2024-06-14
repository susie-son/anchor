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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.toKotlinInstant
import nl.jacobras.humanreadable.HumanReadable
import java.text.DateFormat

@Composable
fun ExposuresScreen(
    modifier: Modifier = Modifier,
    onItemSelect: (String) -> Unit,
    setTopAppBarState: (TopAppBarState) -> Unit,
    viewModel: ExposuresViewModel = hiltViewModel()
) {
    setTopAppBarState(
        TopAppBarState(
            title = R.string.app_name,
            onAction = viewModel::addExposure
        )
    )

    val exposureId by viewModel.exposureId.collectAsState()

    LaunchedEffect(exposureId) {
        exposureId?.let {
            onItemSelect(it)
            viewModel.resetExposureId()
        }
    }

    val exposuresState by viewModel.exposures.collectAsState()

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
            onItemClick = onItemSelect
        )
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
            val exposure = exposures[index]
            val timestamp = exposure.updatedAt

            val timestampText: Flow<String> = flow {
                while (true) {
                    if (timestamp != null) {
                        val time = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            HumanReadable.timeAgo(timestamp.toInstant().toKotlinInstant())
                        } else {
                            DateFormat.getDateTimeInstance().format(timestamp.toDate())
                        }
                        emit(time)
                        delay(1000 * 60)
                    } else {
                        return@flow
                    }
                }
            }

            val time by timestampText.collectAsState(null)

            ListItem(
                overlineContent = { time?.let { StatusWithTimestamp(time = it, status = exposure.status) } },
                headlineContent = { Text(exposure.title.ifBlank { stringResource(R.string.exposures_no_title) }) },
                supportingContent = { Text(exposure.description.ifBlank { stringResource(R.string.exposures_no_description) }) },
                modifier = Modifier.clickable { onItemClick(exposure.id) }
            )
            HorizontalDivider()
        }
    }
}

@Composable
fun StatusWithTimestamp(
    time: String,
    status: Status
) {
    val statusText = stringResource(
        when (status) {
            Status.COMPLETED -> R.string.exposure_completed_date
            Status.IN_PROGRESS -> R.string.exposure_in_progress_date
            Status.READY -> R.string.exposure_ready_date
            Status.DRAFT -> R.string.exposure_draft_date
        },
        time
    )
    Text(statusText, style = MaterialTheme.typography.bodySmall)
}