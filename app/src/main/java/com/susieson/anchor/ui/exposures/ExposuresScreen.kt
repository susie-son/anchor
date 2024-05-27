package com.susieson.anchor.ui.exposures

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.susieson.anchor.model.Exposure
import com.susieson.anchor.model.Status
import kotlinx.datetime.toKotlinInstant
import nl.jacobras.humanreadable.HumanReadable
import java.text.DateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(R.string.exposures_top_bar_title)) },
        modifier = modifier
    )
}

@Composable
fun ExposuresScreen(
    modifier: Modifier = Modifier,
    userId: String,
    onStart: (String, String?) -> Unit = { _, _ -> },
    onItemClick: (String, String, Status) -> Unit = { _, _, _ -> },
    exposuresViewModel: ExposuresViewModel = hiltViewModel()
) {
    val exposures by exposuresViewModel.exposures.collectAsState(emptyList())

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { HomeTopBar() },
        floatingActionButton = {
            if (exposures.isNotEmpty()) {
                ExtendedFloatingActionButton(
                    onClick = { onStart(userId, null) },
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_add),
                            contentDescription = null,
                            modifier = modifier.padding(end = 8.dp)
                        )
                        Text(stringResource(R.string.exposures_start_button))
                    }
                )
            }
        }
    ) { innerPadding ->
        if (exposures.isEmpty()) {
            EmptyExposureList(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                onStart = { onStart(userId, null) }
            )
        } else {
            ExposureList(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                exposures = exposures,
                onItemClick = { exposureId, status -> onItemClick(userId, exposureId, status) }
            )
        }
    }
    LaunchedEffect(true) {
        exposuresViewModel.get(userId)
    }
}

@Composable
fun EmptyExposureList(modifier: Modifier = Modifier, onStart: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.illustration_sailboat),
            modifier = Modifier
                .padding(0.dp, 24.dp)
                .weight(0.8f),
            contentDescription = null,
        )
        Text(stringResource(R.string.exposures_title), style = MaterialTheme.typography.titleLarge)
        Text(stringResource(R.string.exposures_body), style = MaterialTheme.typography.bodyLarge)
        Button(onClick = onStart, modifier = Modifier.padding(vertical = 24.dp)) {
            Text(stringResource(R.string.exposures_start_button))
        }
        Spacer(modifier = Modifier.weight(0.2f))
    }
}

@Composable
fun ExposureList(
    modifier: Modifier = Modifier,
    exposures: List<Exposure>,
    onItemClick: (String, Status) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(exposures.size) { index ->
            ListItem(
                overlineContent = {
                    val timestamp =
                        exposures[index].review.createdAt ?: exposures[index].preparation.createdAt
                        ?: exposures[index].createdAt
                    timestamp?.let {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            return@let HumanReadable.timeAgo(it.toInstant().toKotlinInstant())
                        } else {
                            return@let DateFormat.getDateTimeInstance().format(it.toDate())
                        }
                    }?.let {
                        if (exposures[index].status == Status.COMPLETED) {
                            Text(stringResource(R.string.exposure_completed_date, it))
                        } else {
                            Text(stringResource(R.string.exposure_in_progress_date, it))
                        }
                    }
                },
                headlineContent = { Text(exposures[index].title) },
                supportingContent = { Text(exposures[index].description) },
                modifier = Modifier.clickable {
                    onItemClick(exposures[index].id, exposures[index].status)
                }
            )
            HorizontalDivider()
        }
    }
}