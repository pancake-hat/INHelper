package com.example.inhelper.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.inhelper.features.eureka.EurekaScreen

@Composable
fun INHelperApp() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp),
    ) { innerPadding ->
        EurekaScreen(
            modifier = Modifier.padding(innerPadding)
        )
    }
}
