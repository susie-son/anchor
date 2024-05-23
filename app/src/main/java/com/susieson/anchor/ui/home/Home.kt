package com.susieson.anchor.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.susieson.anchor.AnchorApp
import com.susieson.anchor.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = { Text(text = "Anchor") },
        modifier = modifier
    )
}

@Composable
fun Home(modifier: Modifier = Modifier) {
    EmptyVoyageList(modifier = modifier)
}

@Composable
fun EmptyVoyageList(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.sailboat_illus),
            modifier = Modifier.padding(0.dp, 24.dp),
            contentDescription = null,
        )
        Text(stringResource(R.string.home_title), style = MaterialTheme.typography.titleLarge)
        Text(stringResource(R.string.home_body), style = MaterialTheme.typography.bodyLarge)
        Button(onClick = { /*TODO*/ }, modifier = Modifier.padding(0.dp, 24.dp)) {
            Text(stringResource(R.string.home_start_button))
        }
    }
}

@Preview
@Composable
fun HomePreview() {
    AnchorApp(
        topBar = { HomeTopBar() },
        content = { Home(modifier = it) }
    )
}