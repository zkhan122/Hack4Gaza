// com/example/nanoMedic/ui/screens/GuideScreen.kt
package com.example.nanomedic.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nanomedic.guides.WoundGuideLookup
import com.example.nanomedic.guides.WoundGuideJsonEntry

@Composable
fun GuideScreen(
    woundType: String,
    onNavigateBackToCameraScreen: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detailed Guide: $woundType") },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBackToCameraScreen() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        val context = LocalContext.current
        val woundGuideLookup = remember { WoundGuideLookup(context) }
        val guide: WoundGuideJsonEntry? = remember(woundType) {
            woundGuideLookup.getWoundGuideByType(woundType)
        }

        val listState = rememberLazyListState()

        Box(modifier = Modifier.padding(innerPadding)) {
            if (guide != null) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = "Wound Type: ${guide.woundType}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = "Identification (English):",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = guide.identificationEng,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = "Identification (Arabic):",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = guide.identificationArab,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(Modifier.height(16.dp))

                            Text(
                                text = "Treatment Steps (English):",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            guide.treatmentEng.forEach { stepsMap ->
                                stepsMap.entries.sortedBy { it.key }
                                    .forEach { (stepKey, stepText) ->
                                        Text(
                                            text = "${stepKey.replace("Step-", "")}. $stepText", // Format "Step-1" to "1."
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.padding(start = 8.dp)
                                        )
                                    }
                            }
                            Spacer(Modifier.height(16.dp))

                            Text(
                                text = "Treatment Steps (Arabic):",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            guide.treatmentArab.forEach { stepsMap ->
                                stepsMap.entries.sortedBy { it.key }
                                    .forEach { (stepKey, stepText) ->
                                        Text(
                                            text = "${stepKey.replace("Step-", "")}. $stepText",
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.padding(start = 8.dp)
                                        )
                                    }
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator() // Or Text("Guide not found for $woundType")
                    Text("Loading guide for $woundType...", modifier = Modifier.padding(top = 70.dp))
                }
            }


            val isScrolledToTop by remember { derivedStateOf { listState.firstVisibleItemIndex > 0 } }
            val isScrolledToBottom by remember { derivedStateOf { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index != listState.layoutInfo.totalItemsCount - 1 } }

            AnimatedVisibility(
                visible = isScrolledToBottom,
                modifier = Modifier.align(Alignment.BottomCenter),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Fade(isTop = false)
            }

            AnimatedVisibility(
                visible = isScrolledToTop,
                modifier = Modifier.align(Alignment.TopCenter),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Fade(isTop = true)
            }
        }
    }
}

@Composable
private fun Fade(isTop: Boolean) {
    val gradientColors = listOf(
        MaterialTheme.colorScheme.background,
        MaterialTheme.colorScheme.background.copy(alpha = 0.0f)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = if (isTop) gradientColors.reversed() else gradientColors
                )
            )
    )
}