package com.example.nanomedic

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import androidx.core.graphics.scale



class SkinDiseaseClassifier(private val context: Context) {
    private var interpreter: Interpreter? = null
    private var inputImageWidth: Int = 0
    private var inputImageHeight: Int = 0
    private var modelInputSize: Int = 0

    // Update these with your actual disease names
    private val diseaseLabels = arrayOf(
        "Abrasions",
        "Bruises",
        "Burns",
        "Cut",
        "Ingrown_nails",
        "Laceration",
        "Stab_wound"
    )

    fun initializeInterpreter() {
        try {
            val assetManager = context.assets
            val model = loadModelFile(assetManager, "final_model.tflite")
//            interpreter = Interpreter(model)
            val options = Interpreter.Options().apply {
                setNumThreads(4)
                setUseNNAPI(true)
                setUseXNNPACK(true)
            }
            interpreter = Interpreter(model, options)


            val inputShape = interpreter!!.getInputTensor(0).shape()
            inputImageWidth = inputShape[1]
            inputImageHeight = inputShape[2]
            modelInputSize = FLOAT_TYPE_SIZE * inputImageWidth * inputImageHeight * PIXEL_SIZE

            Log.d("SkinClassifier", "Model loaded: ${inputImageWidth}x${inputImageHeight}")
        } catch (e: Exception) {
            Log.e("SkinClassifier", "Error loading model", e)
        }
    }

    private fun loadModelFile(assetManager: android.content.res.AssetManager, filename: String): ByteBuffer {
        val fileDescriptor = assetManager.openFd(filename)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    @SuppressLint("DefaultLocale")
    fun classify(bitmap: Bitmap): String {
        if (interpreter == null) {
            return "Error: Model not loaded"
        }

        try {
            val resizedImage = bitmap.scale(inputImageWidth, inputImageHeight)

            val byteBuffer = convertBitmapToByteBuffer(resizedImage)
            val output = Array(1) { FloatArray(diseaseLabels.size) }

            interpreter?.run(byteBuffer, output)

            val result = output[0]
            val maxIndex = result.indices.maxBy { result[it] } ?: -1
            val confidence = result[maxIndex] * 100

            return "Prediction: ${diseaseLabels[maxIndex]}\nConfidence: ${String.format("%.1f", confidence)}%"

        } catch (e: Exception) {
            Log.e("SkinClassifier", "Classification error", e)
            return "Error: Classification failed"
        }
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(modelInputSize)
        byteBuffer.order(ByteOrder.nativeOrder())

        val pixels = IntArray(inputImageWidth * inputImageHeight)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        for (pixelValue in pixels) {
            val r = (pixelValue shr 16 and 0xFF)
            val g = (pixelValue shr 8 and 0xFF)
            val b = (pixelValue and 0xFF)

            byteBuffer.putFloat(r / 255.0f)
            byteBuffer.putFloat(g / 255.0f)
            byteBuffer.putFloat(b / 255.0f)
        }

        return byteBuffer
    }

    fun close() {
        interpreter?.close()
        interpreter = null
    }

    companion object {
        private const val FLOAT_TYPE_SIZE = 4
        private const val PIXEL_SIZE = 3
    }
}