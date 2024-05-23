package com.susieson.anchor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.susieson.anchor.ui.home.Home
import com.susieson.anchor.ui.home.HomeTopBar
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
fun AnchorApp(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = { HomeTopBar() },
    content: @Composable (modifier: Modifier) -> Unit = {
        Home(modifier = it)
    }
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = topBar
    ) { innerPadding ->
        content(modifier.fillMaxSize().padding(innerPadding))
    }
}

@Preview
@Composable
fun AppPreview() {
    AnchorTheme {
        AnchorApp()
    }
}