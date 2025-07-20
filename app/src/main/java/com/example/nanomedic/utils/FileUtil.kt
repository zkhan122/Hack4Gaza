package com.example.nanomedic.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.nanomedic.R // Make sure to import your app's R file
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = getExternalFilesDir("Pictures") // This should match the path in file_paths.xml
    return File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        storageDir      /* directory */
    )
}

fun Context.getUriForFile(file: File): Uri {
    return FileProvider.getUriForFile(
        this,
        "${packageName}.provider", // This must match the authorities in AndroidManifest.xml
        file
    )
}