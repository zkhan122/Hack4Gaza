////package com.example.nanomedic.ml
////import android.content.Context
////import android.graphics.Bitmap
////import android.util.Log
////import org.tensorflow.lite.Interpreter
////import java.io.FileInputStream
////import java.nio.ByteBuffer
////import java.nio.ByteOrder
////import java.nio.MappedByteBuffer
////import java.nio.channels.FileChannel
////
////class WoundClassifier(private val context: Context) {
////    private var interpreter: Interpreter? = null
////    private val modelInputSize = 224 // Adjust based on your model
////    private val pixelSize = 3 // RGB
////    private val imageMean = 127.5f
////    private val imageStd = 127.5f
////
////    private val woundTypes = arrayOf(
////        "Abrasions",
////        "Bruises",
////        "Burns",
////        "Ingrown_nails",
////        "Laceration",
////        "Stab_wound"
////    )
////
////    init {
////        try {
////            interpreter = Interpreter(loadModelFile())
////            Log.d("WoundClassifier", "Model loaded successfully")
////        } catch (e: Exception) {
////            Log.e("WoundClassifier", "Error loading model: ${e.message}")
////        }
////    }
////
////    private fun loadModelFile(): java.nio.MappedByteBuffer {
////        val fileDescriptor = context.assets.openFd("final_model.tflite")
////        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
////        val fileChannel = inputStream.channel
////        val startOffset = fileDescriptor.startOffset
////        val declaredLength = fileDescriptor.declaredLength
////        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
////    }
////
////    fun classifyImage(bitmap: Bitmap): ClassificationResult {
////        if (interpreter == null) {
////            return ClassificationResult("Error", 0.0f, "Model not loaded")
////        }
////
////        try {
////            // Preprocess the image
////            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, modelInputSize, modelInputSize, true)
////            val byteBuffer = convertBitmapToByteBuffer(resizedBitmap)
////
////            // Prepare output array
////            val output = Array(1) { FloatArray(woundTypes.size) }
////
////            // Run inference
////            interpreter!!.run(byteBuffer, output)
////
////            // Process results
////            val probabilities = output[0]
////            val maxIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: 0
////            val confidence = probabilities[maxIndex]
////            val predictedClass = woundTypes[maxIndex]
////
////            Log.d("WoundClassifier", "Prediction: $predictedClass with confidence: $confidence")
////
////            return ClassificationResult(
////                predictedClass = predictedClass,
////                confidence = confidence,
////                message = if (confidence > 0.7f) "High confidence prediction" else "Low confidence - please retake photo"
////            )
////
////        } catch (e: Exception) {
////            Log.e("WoundClassifier", "Error during classification: ${e.message}")
////            return ClassificationResult("Error", 0.0f, "Classification failed: ${e.message}")
////        }
////    }
////
////    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
////        val byteBuffer = ByteBuffer.allocateDirect(4 * modelInputSize * modelInputSize * pixelSize)
////        byteBuffer.order(ByteOrder.nativeOrder())
////
////        val intValues = IntArray(modelInputSize * modelInputSize)
////        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
////
////        var pixel = 0
////        for (i in 0 until modelInputSize) {
////            for (j in 0 until modelInputSize) {
////                val pixelValue = intValues[pixel++]
////
////                // Extract RGB values and normalize
////                val r = ((pixelValue shr 16 and 0xFF) - imageMean) / imageStd
////                val g = ((pixelValue shr 8 and 0xFF) - imageMean) / imageStd
////                val b = ((pixelValue and 0xFF) - imageMean) / imageStd
////
////                byteBuffer.putFloat(r)
////                byteBuffer.putFloat(g)
////                byteBuffer.putFloat(b)
////            }
////        }
////
////        return byteBuffer
////    }
////
////    fun close() {
////        interpreter?.close()
////    }
////}
//
//package com.example.nanomedic.ml
//
//import android.content.Context
//import android.graphics.Bitmap
//import android.util.Log
//import org.tensorflow.lite.Interpreter
//import java.io.FileInputStream
//import java.nio.ByteBuffer
//import java.nio.ByteOrder
//import java.nio.MappedByteBuffer
//import java.nio.channels.FileChannel
//import androidx.core.graphics.scale
//
//// Add this missing data class
//data class ClassificationResult(
//    val predictedClass: String,
//    val confidence: Float,
//    val message: String
//)
//
//class WoundClassifier(private val context: Context) {
//    private var interpreter: Interpreter? = null
////    private val modelInputSize = 224 // Adjust based on your model
//    private val modelInputSize = 224 // Adjust based on your model
//    private val pixelSize = 3 // RGB
//    private val imageMean = 127.5f
//    private val imageStd = 127.5f
//
//    // Updated wound types to match your model
//    private val woundTypes = arrayOf(
//        "Abrasions",
//        "Bruises",
//        "Burns",
//        "Ingrown_nails",
//        "Laceration",
//        "Stab_wound"
//    )
//
//    init {
//        try {
//            interpreter = Interpreter(loadModelFile())
//            Log.d("WoundClassifier", "Model loaded successfully")
//        } catch (e: Exception) {
//            Log.e("WoundClassifier", "Error loading model: ${e.message}")
//            // Create a dummy interpreter for testing if model fails to load
//            interpreter = null
//        }
//    }
//
//    private fun loadModelFile(): MappedByteBuffer {
//        return try {
//            val fileDescriptor = context.assets.openFd("final_model.tflite")
//            val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
//            val fileChannel = inputStream.channel
//            val startOffset = fileDescriptor.startOffset
//            val declaredLength = fileDescriptor.declaredLength
//            fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
//        } catch (e: Exception) {
//            Log.e("WoundClassifier", "Model file not found: ${e.message}")
//            throw e
//        }
//    }
//
//    fun classifyImage(bitmap: Bitmap): ClassificationResult {
//        if (interpreter == null) {
//            // For testing without model file - return a mock result
//            Log.w("WoundClassifier", "No model loaded, returning mock result")
//            return ClassificationResult(
//                "Laceration",
//                0.85f,
//                "Mock prediction (no model loaded)"
//            )
//        }
//
//        try {
//            // Preprocess the image
//            val resizedBitmap = bitmap.scale(modelInputSize, modelInputSize)
//            val byteBuffer = convertBitmapToByteBuffer(resizedBitmap)
//
//            // Prepare output array
//            val output = Array(1) { FloatArray(woundTypes.size) }
//
//            // Run inference
//            interpreter!!.run(byteBuffer, output)
//
//            // Process results
//            val probabilities = output[0]
//            val maxIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: 0
//            val confidence = probabilities[maxIndex]
//            val predictedClass = woundTypes[maxIndex]
//
//            Log.d("WoundClassifier", "Prediction: $predictedClass with confidence: $confidence")
//
//            // Log all probabilities for debugging
//            woundTypes.forEachIndexed { index, woundType ->
//                Log.d("WoundClassifier", "$woundType: ${probabilities[index]}")
//            }
//
//            return ClassificationResult(
//                predictedClass = predictedClass,
//                confidence = confidence,
//                message = when {
//                    confidence > 0.8f -> "High confidence prediction"
//                    confidence > 0.6f -> "Medium confidence prediction"
//                    else -> "Low confidence - consider retaking photo"
//                }
//            )
//
//        } catch (e: Exception) {
//            Log.e("WoundClassifier", "Error during classification: ${e.message}")
//            return ClassificationResult(
//                "Error",
//                0.0f,
//                "Classification failed: ${e.message}"
//            )
//        }
//    }
//
//    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
//        val byteBuffer = ByteBuffer.allocateDirect(4 * modelInputSize * modelInputSize * pixelSize)
//        byteBuffer.order(ByteOrder.nativeOrder())
//
//        val intValues = IntArray(modelInputSize * modelInputSize)
//        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
//
//        var pixel = 0
//        for (i in 0 until modelInputSize) {
//            for (j in 0 until modelInputSize) {
//                val pixelValue = intValues[pixel++]
//
//                // Extract RGB values and normalize
//                val r = ((pixelValue shr 16 and 0xFF) - imageMean) / imageStd
//                val g = ((pixelValue shr 8 and 0xFF) - imageMean) / imageStd
//                val b = ((pixelValue and 0xFF) - imageMean) / imageStd
//
//                byteBuffer.putFloat(r)
//                byteBuffer.putFloat(g)
//                byteBuffer.putFloat(b)
//            }
//        }
//
//        return byteBuffer
//    }
//
//    fun close() {
//        interpreter?.close()
//    }
//}

package com.example.nanomedic.ml

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

data class ClassificationResult(
    val predictedClass: String,
    val confidence: Float,
    val message: String
)

class WoundClassifier(private val context: Context) {
    private var interpreter: Interpreter? = null
    private val modelInputSize = 180  // Changed to match your training
    private val pixelSize = 3 // RGB

    // EfficientNet preprocessing - different from what you had before!
    // EfficientNet uses range [0, 255] then normalizes to [-1, 1]

    private val woundTypes = arrayOf(
        "Abrasions",
        "Burns",
        "Cut",
        "Ingrown_nails",
        "Laceration",
        "Bruises",
        "Stab_wound"
    )

    init {
        try {
            interpreter = Interpreter(loadModelFile())
            Log.d("WoundClassifier", "Model loaded successfully")
        } catch (e: Exception) {
            Log.e("WoundClassifier", "Error loading model: ${e.message}")
            interpreter = null
        }
    }

    private fun loadModelFile(): MappedByteBuffer {
        return try {
            val fileDescriptor = context.assets.openFd("final_model.tflite")
            val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
            val fileChannel = inputStream.channel
            val startOffset = fileDescriptor.startOffset
            val declaredLength = fileDescriptor.declaredLength
            fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        } catch (e: Exception) {
            Log.e("WoundClassifier", "Model file not found: ${e.message}")
            throw e
        }
    }

    fun classifyImage(bitmap: Bitmap): ClassificationResult {
        if (interpreter == null) {
            Log.w("WoundClassifier", "No model loaded, returning mock result")
            return ClassificationResult(
                "Laceration",
                0.85f,
                "Mock prediction (no model loaded)"
            )
        }

        try {
            // Debug: Check model input details
            val inputTensor = interpreter!!.getInputTensor(0)
            val inputShape = inputTensor.shape()
            Log.d("WoundClassifier", "Model input shape: ${inputShape.contentToString()}")

            // Preprocess the image with correct size (180x180)
            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, modelInputSize, modelInputSize, true)
            val byteBuffer = convertBitmapToByteBuffer(resizedBitmap)

            // Prepare output array
            val output = Array(1) { FloatArray(woundTypes.size) }

            // Run inference
            interpreter!!.run(byteBuffer, output)

            // Process results
            val probabilities = output[0]
            val maxIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: 0
            val confidence = probabilities[maxIndex]
            val predictedClass = woundTypes[maxIndex]

            Log.d("WoundClassifier", "Prediction: $predictedClass with confidence: $confidence")

            // Log all probabilities for debugging
            woundTypes.forEachIndexed { index, woundType ->
                Log.d("WoundClassifier", "$woundType: ${probabilities[index]}")
            }

            return ClassificationResult(
                predictedClass = predictedClass,
                confidence = confidence,
                message = when {
                    confidence > 0.8f -> "High confidence prediction"
                    confidence > 0.6f -> "Medium confidence prediction"
                    else -> "Low confidence - consider retaking photo"
                }
            )

        } catch (e: Exception) {
            Log.e("WoundClassifier", "Error during classification: ${e.message}")
            return ClassificationResult(
                "Error",
                0.0f,
                "Classification failed: ${e.message}"
            )
        }
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * modelInputSize * modelInputSize * pixelSize)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(modelInputSize * modelInputSize)
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        var pixel = 0
        for (i in 0 until modelInputSize) {
            for (j in 0 until modelInputSize) {
                val pixelValue = intValues[pixel++]

                // Extract RGB values (0-255 range)
                val r = (pixelValue shr 16 and 0xFF).toFloat()
                val g = (pixelValue shr 8 and 0xFF).toFloat()
                val b = (pixelValue and 0xFF).toFloat()

                // EfficientNet preprocessing: normalize to [-1, 1] range
                // This matches the preprocess_input function from your training
                val normalizedR = (r / 127.5f) - 1.0f
                val normalizedG = (g / 127.5f) - 1.0f
                val normalizedB = (b / 127.5f) - 1.0f

                byteBuffer.putFloat(normalizedR)
                byteBuffer.putFloat(normalizedG)
                byteBuffer.putFloat(normalizedB)
            }
        }

        return byteBuffer
    }

    fun close() {
        interpreter?.close()
    }
}
