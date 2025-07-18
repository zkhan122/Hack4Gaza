package com.example.nanoMedic.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.dp

@Composable
fun GuideScreen(onNavigateBackToCameraScreen: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detailed Guide") },
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

        val listState = rememberLazyListState()

        Box(modifier = Modifier.padding(innerPadding)) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                items(20) {
                    Text(
                        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras elementum ultrices diam. Maecenas ligula massa, varius a, semper congue, euismod non, mi. Proin porttitor, orci nec nonummy molestie, enim est eleifend mi, non fermentum diam nisl sit amet erat.",
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
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