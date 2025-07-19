package com.example.nanomedic

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nanomedic.ui.screens.CameraScreen
import com.example.nanoMedic.ui.screens.GuideScreen
import com.example.nanomedic.ui.screens.LoadingScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Camera.route) {
        composable(Screen.Camera.route) {
            CameraScreen(
                onNavigateToLoadingScreen = {
                    navController.navigate(Screen.Loading.route)
                }
            )
        }

        composable(Screen.Loading.route) {
            LoadingScreen(
                onNavigateToGuide = {
                    navController.navigate(Screen.Guide.route) {
                        popUpTo(Screen.Camera.route)
                    }
                }
            )
        }

        composable(Screen.Guide.route) {
            GuideScreen(
                onNavigateBackToCameraScreen = {
                    navController.navigateUp()
                }
            )
        }

    }
}