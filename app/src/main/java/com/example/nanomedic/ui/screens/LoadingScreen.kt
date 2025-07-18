package com.example.nanomedic.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(onNavigateToGuide : () -> Unit) {
    // Safely run side-effects like timers or network calls when a composable first appears on screen.
    // The key `true` means this effect will only run ONCE.
    LaunchedEffect(key1 = true) {
        delay(2000) // Simulate a 2-second network or processing delay.
        onNavigateToGuide() // After the delay, trigger the navigation.
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Text(text = "Processing...", modifier = Modifier.padding(top = 16.dp))
    }
}