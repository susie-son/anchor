package com.susieson.anchor.ui.home

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.susieson.anchor.R
import com.susieson.anchor.model.Voyage
import com.susieson.anchor.ui.theme.AnchorTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(R.string.home_top_bar_title)) }, modifier = modifier
    )
}

@Composable
fun Home(
    modifier: Modifier = Modifier,
    onStart: () -> Unit = {},
    onItemClick: (Voyage) -> Unit = {},
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val voyages by homeViewModel.voyages.collectAsState(null)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { HomeTopBar() },
        floatingActionButton = {
            if (voyages?.isNotEmpty() == true) {
                ExtendedFloatingActionButton(
                    onClick = onStart,
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_boat),
                            contentDescription = null,
                            modifier = modifier.padding(end = 8.dp)
                        )
                        Text(stringResource(R.string.home_start_button))
                    }
                )
            }
        }
    ) { innerPadding ->
        voyages?.let {
            if (it.isEmpty()) {
                EmptyVoyageList(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    onStart = onStart
                )
            } else {
                VoyageList(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    voyages = it,
                    onItemClick = onItemClick
                )
            }
        }
    }
    LaunchedEffect(true) {
        homeViewModel.login()
    }
}

@Composable
fun EmptyVoyageList(modifier: Modifier = Modifier, onStart: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.illustration_sailboat),
            modifier = Modifier.padding(0.dp, 24.dp).weight(0.8f),
            contentDescription = null,
        )
        Text(stringResource(R.string.home_title), style = MaterialTheme.typography.titleLarge)
        Text(stringResource(R.string.home_body), style = MaterialTheme.typography.bodyLarge)
        Button(onClick = onStart, modifier = Modifier.padding(vertical = 24.dp)) {
            Text(stringResource(R.string.home_start_button))
        }
        Spacer(modifier = Modifier.weight(0.2f))
    }
}

@Composable
fun VoyageList(modifier: Modifier = Modifier, voyages: List<Voyage>, onItemClick: (Voyage) -> Unit) {
    LazyColumn(modifier = modifier) {
        items(voyages.size) { index ->
            ListItem(
                headlineContent = { Text(voyages[index].title) },
                supportingContent = { Text(voyages[index].description) },
                modifier = Modifier.clickable {
                    onItemClick(voyages[index])
                }
            )
            HorizontalDivider()
        }
    }
}

@Preview
@Composable
fun HomePreview() {
    AnchorTheme {
        Home()
    }
}