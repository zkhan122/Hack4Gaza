package com.example.nanomedic.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nanomedic.guides.WoundGuideJsonEntry
import com.example.nanomedic.guides.WoundGuideLookup

@Composable
fun GuideScreen(
    woundType: String,
    onNavigateBackToCameraScreen: () -> Unit
) {
    var isEnglish by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detailed Guide") },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBackToCameraScreen() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Text("EN", style = MaterialTheme.typography.labelMedium)
                        Switch(
                            checked = !isEnglish,
                            onCheckedChange = { isEnglish = !it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary
                            )
                        )
                        Text("AR", style = MaterialTheme.typography.labelMedium)
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
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Wound Type:",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = guide.woundType,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }

                        // Identification
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = if (isEnglish) "Identification (English):" else "الوصف (العربية):",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (isEnglish) guide.identificationEng else guide.identificationArab,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }

                        // Treatment Steps
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = if (isEnglish) "Treatment Steps (English):" else "خطوات العلاج (العربية):",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                val treatmentList = if (isEnglish) guide.treatmentEng else guide.treatmentArab
                                treatmentList.forEach { stepsMap ->
                                    stepsMap.entries.sortedBy { it.key }
                                        .forEach { (stepKey, stepText) ->
                                            Text(
                                                text = "${stepKey.replace("Step-", "")}. $stepText",
                                                style = MaterialTheme.typography.bodyMedium,
                                                modifier = Modifier.padding(start = 8.dp, bottom = 6.dp)
                                            )
                                        }
                                }
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Loading guide for $woundType...")
                    }
                }
            }

            // Scroll fade indicators
            val isScrolledToTop by remember { derivedStateOf { listState.firstVisibleItemIndex > 0 } }
            val isScrolledToBottom by remember {
                derivedStateOf {
                    listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index !=
                            listState.layoutInfo.totalItemsCount - 1
                }
            }

            AnimatedVisibility(
                visible = isScrolledToBottom,
                modifier = Modifier.align(Alignment.BottomCenter),
                enter = fadeIn(), exit = fadeOut()
            ) {
                Fade(isTop = false)
            }

            AnimatedVisibility(
                visible = isScrolledToTop,
                modifier = Modifier.align(Alignment.TopCenter),
                enter = fadeIn(), exit = fadeOut()
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
