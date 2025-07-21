
package com.example.nanomedic

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanomedic.ml.ClassificationResult
import com.example.nanomedic.ml.WoundClassifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhotoViewModel : ViewModel() {

    private val _capturedPhoto = MutableStateFlow<Bitmap?>(null)
    val capturedPhoto: StateFlow<Bitmap?> = _capturedPhoto.asStateFlow()

    private val _classificationResult = MutableStateFlow<ClassificationResult?>(null)
    val classificationResult: StateFlow<ClassificationResult?> = _classificationResult.asStateFlow()

    private val _isClassifying = MutableStateFlow(false)
    val isClassifying: StateFlow<Boolean> = _isClassifying.asStateFlow()

    private var woundClassifier: WoundClassifier? = null

    fun initializeClassifier(woundClassifier: WoundClassifier) {
        this.woundClassifier = woundClassifier
    }

    fun setPhoto(bitmap: Bitmap) {
        _capturedPhoto.value = bitmap
    }

    fun classifyPhoto() {
        val photo = _capturedPhoto.value
        val classifier = woundClassifier

        if (photo == null || classifier == null) {
            _classificationResult.value = ClassificationResult(
                "Error",
                0.0f,
                "No photo or classifier available"
            )
            return
        }

        viewModelScope.launch {
            _isClassifying.value = true

            try {
                // Run classification on background thread
                val result = withContext(Dispatchers.Default) {
                    classifier.classifyImage(photo)
                }
                _classificationResult.value = result
            } catch (e: Exception) {
                _classificationResult.value = ClassificationResult(
                    "Error",
                    0.0f,
                    "Classification error: ${e.message}"
                )
            } finally {
                _isClassifying.value = false
            }
        }
    }

    fun clearPhoto() {
        _capturedPhoto.value = null
        _classificationResult.value = null
    }

    override fun onCleared() {
        super.onCleared()
        woundClassifier?.close()
    }
}
