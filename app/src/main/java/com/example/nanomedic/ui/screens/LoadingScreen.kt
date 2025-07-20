
package com.example.nanomedic.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nanomedic.PhotoViewModel

@Composable
fun LoadingScreen(onNavigateToGuide: (String) -> Unit, photoViewModel: PhotoViewModel) {

    val classificationResult by photoViewModel.classificationResult.collectAsState()
    val isClassifying by photoViewModel.isClassifying.collectAsState()

    // Start classification when screen loads
    LaunchedEffect(key1 = true) {
        photoViewModel.classifyPhoto()
    }

    // Navigate when classification is complete
    LaunchedEffect(classificationResult) {
        classificationResult?.let { result ->
            // Navigate with the predicted wound type
            onNavigateToGuide(result.predictedClass)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Text(
            text = if (isClassifying) "Analyzing wound..." else "Processing...",
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}