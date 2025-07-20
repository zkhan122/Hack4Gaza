package com.example.nanomedic.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.nanomedic.utils.createImageFile
import com.example.nanomedic.utils.getUriForFile
import java.util.Objects

@Composable
fun CameraScreen(onNavigateToLoadingScreen: () -> Unit) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var tempImageFile by remember { mutableStateOf<java.io.File?>(null) }

    // Launcher for taking a picture
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                // The picture was taken successfully, imageUri contains the content URI
                Toast.makeText(context, "Picture saved successfully!", Toast.LENGTH_SHORT).show()
                // You can now use the imageUri for display or further processing
                // For example, navigate to the next screen
                onNavigateToLoadingScreen()
            } else {
                // The user cancelled the action. We can optionally delete the temp file.
                tempImageFile?.delete()
                Toast.makeText(context, "Picture capture cancelled.", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // Launcher for camera permission
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Permission granted, launch the camera
                val file = context.createImageFile()
                tempImageFile = file
                val uri = context.getUriForFile(file)
                imageUri = uri
                cameraLauncher.launch(uri)
            } else {
                // Permission denied
                Toast.makeText(context, "Camera permission denied.", Toast.LENGTH_SHORT).show()
            }
        }
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) -> {
                    // Permission is already granted, launch the camera
                    val file = context.createImageFile()
                    tempImageFile = file
                    val uri = context.getUriForFile(file)
                    imageUri = uri
                    cameraLauncher.launch(uri)
                }
                else -> {
                    // Request the permission
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }
        }) {
            Text("Take Picture")
        }
    }
}