

package com.example.nanomedic.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.nanomedic.PhotoViewModel
import com.example.nanomedic.utils.createImageFile
import com.example.nanomedic.utils.getUriForFile
import java.io.InputStream

@Composable
fun CameraScreen(
    onNavigateToLoadingScreen: () -> Unit,
    photoViewModel: PhotoViewModel
) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var tempImageFile by remember { mutableStateOf<java.io.File?>(null) }
    var showPreview by remember { mutableStateOf(false) }

    // Launcher for taking a picture
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                // Convert URI to Bitmap for preview and ML processing
                imageUri?.let { uri ->
                    try {
                        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        inputStream?.close()

                        if (bitmap != null) {
                            capturedBitmap = bitmap  // Update state variable
                            showPreview = true
                            Toast.makeText(context, "Picture captured! Choose an option.", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error loading image", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                // User cancelled the action
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
                launchCamera(context) { file, uri ->
                    tempImageFile = file
                    imageUri = uri
                    cameraLauncher.launch(uri)
                }
            } else {
                Toast.makeText(context, "Camera permission denied.", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // Main UI
    Box(modifier = Modifier.fillMaxSize()) {
        if (showPreview && capturedBitmap != null) {
            // Photo Preview with Tick/Cross UI
            PhotoPreviewWithActions(
                bitmap = capturedBitmap!!,
                onAccept = {
                    // User clicked tick - accept photo
                    photoViewModel.setPhoto(capturedBitmap!!)
                    tempImageFile?.delete() // Clean up temp file
                    onNavigateToLoadingScreen()
                },
                onRetake = {
                    // User clicked cross - retake photo
                    capturedBitmap = null
                    showPreview = false
                    tempImageFile?.delete()
                    // Immediately launch camera again for retake
                    launchCamera(context) { file, uri ->
                        tempImageFile = file
                        imageUri = uri
                        cameraLauncher.launch(uri)
                    }
                }
            )
        } else {
            // Initial Camera Button UI
            InitialCameraUI(
                onTakePhoto = {
                    when (PackageManager.PERMISSION_GRANTED) {
                        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                            launchCamera(context) { file, uri ->
                                tempImageFile = file
                                imageUri = uri
                                cameraLauncher.launch(uri)
                            }
                        }
                        else -> {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun InitialCameraUI(onTakePhoto: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onTakePhoto,
            modifier = Modifier
                .size(width = 200.dp, height = 60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Take Picture",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun PhotoPreviewWithActions(
    bitmap: Bitmap,
    onAccept: () -> Unit,
    onRetake: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Review Your Photo",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 40.dp, bottom = 20.dp)
        )

        // Photo Preview
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Captured photo",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        // Action Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 40.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Cross (Retake) Button
            FloatingActionButton(
                onClick = onRetake,
                containerColor = Color.Red,
                modifier = Modifier.size(80.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Retake photo",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            // Tick (Accept) Button
            FloatingActionButton(
                onClick = onAccept,
                containerColor = Color.Green,
                modifier = Modifier.size(80.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Accept photo",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        // Helper Text
        Text(
            text = "❌ Retake  •  ✅ Analyze",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
        )
    }
}

private fun launchCamera(
    context: Context,
    onReady: (file: java.io.File, uri: Uri) -> Unit
) {
    val file = context.createImageFile()
    val uri = context.getUriForFile(file)
    onReady(file, uri)
}
