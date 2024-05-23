package com.susieson.anchor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.susieson.anchor.ui.theme.AnchorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnchorTheme {
                AnchorApp()
            }
        }
    }
}

@Composable
fun AnchorApp(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { AnchorTopBar() }
    ) { innerPadding ->
        EmptyVoyageList(modifier = modifier
            .fillMaxSize()
            .padding(innerPadding))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnchorTopBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = { Text(text = "Anchor") },
        modifier = modifier
    )
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

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    AnchorTheme {
        AnchorApp()
    }
}