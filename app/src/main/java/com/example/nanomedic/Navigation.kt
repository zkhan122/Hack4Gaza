//package com.example.nanomedic
//
//import androidx.compose.runtime.Composable
//import androidx.navigation.NavType
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import androidx.navigation.navArgument
//import com.example.nanomedic.ui.screens.CameraScreen
//import com.example.nanomedic.ui.screens.GuideScreen
//import com.example.nanomedic.ui.screens.LoadingScreen
//
//
//@Composable
//fun Navigation(photoViewModel: PhotoViewModel) {
//    val navController = rememberNavController()
//
//    NavHost(navController = navController, startDestination = Screen.Camera.route) {
//        composable(Screen.Camera.route) {
//            CameraScreen(
//                photoViewModel = photoViewModel,  // Pass ViewModel
//                onNavigateToLoadingScreen = {
//                    navController.navigate(Screen.Loading.route)
//                }
//            )
//        }
//
//        composable(Screen.Loading.route) {
//            LoadingScreen(
//                photoViewModel = photoViewModel,  // Pass ViewModel
//                onNavigateToGuide = {
//                    val woundType = "Stab_Wound"  // or get this dynamically
//                    navController.navigate(Screen.Guide.createRoute(woundType)) {
//                        popUpTo(Screen.Camera.route)
//                    }
//                }
//            )
//        }
//
//        composable(
//            route = Screen.Guide.route,
//            arguments = listOf(navArgument("woundType") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val woundType = backStackEntry.arguments?.getString("woundType") ?: ""
//            GuideScreen(
//                woundType = woundType,
//                onNavigateBackToCameraScreen = {
//                    photoViewModel.clearPhoto()
//                    navController.navigateUp()
//                }
//            )
//        }
//
//    }
//}

package com.example.nanomedic

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nanomedic.ui.screens.CameraScreen
import com.example.nanomedic.ui.screens.GuideScreen
import com.example.nanomedic.ui.screens.LoadingScreen

@Composable
fun Navigation(photoViewModel: PhotoViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Camera.route) {
        composable(Screen.Camera.route) {
            CameraScreen(
                photoViewModel = photoViewModel,
                onNavigateToLoadingScreen = {
                    navController.navigate(Screen.Loading.route)
                }
            )
        }

        composable(Screen.Loading.route) {
            LoadingScreen(
                photoViewModel = photoViewModel,
                onNavigateToGuide = { woundType ->
                    // Use actual ML prediction instead of hardcoded value
                    navController.navigate(Screen.Guide.createRoute(woundType)) {
                        popUpTo(Screen.Camera.route)
                    }
                }
            )
        }

        composable(
            route = Screen.Guide.route,
            arguments = listOf(navArgument("woundType") { type = NavType.StringType })
        ) { backStackEntry ->
            val woundType = backStackEntry.arguments?.getString("woundType") ?: ""
            GuideScreen(
                woundType = woundType,
                onNavigateBackToCameraScreen = {
                    photoViewModel.clearPhoto()
                    navController.navigate(Screen.Camera.route) {
                        popUpTo(Screen.Camera.route) { inclusive = true }
                    }
                }
            )
        }
    }
}