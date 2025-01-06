package net.sostaric.geminidemo.ui.image

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ImageViewModel(
    private val generativeModel: GenerativeModel
) : ViewModel() {

    private val _uiState: MutableStateFlow<ImageUiState> = MutableStateFlow(ImageUiState.Initial)
    val uiState: StateFlow<ImageUiState> = _uiState.asStateFlow()

    fun analyzeImage(
        userInput: String,
        selectedImages: List<Bitmap>
    ) {
        _uiState.value = ImageUiState.Loading
        val prompt = "Look at the image(s), and then answer the following question: $userInput"

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputContent = content {
                    for (bitmap in selectedImages) {
                        image(bitmap)
                    }
                    text(prompt)
                }

                var outputContent = ""

                generativeModel.generateContentStream(inputContent)
                    .collect { response ->
                        outputContent += response.text
                        _uiState.value = ImageUiState.Success(outputContent)
                    }
            } catch (e: Exception) {
                _uiState.value = ImageUiState.Error(e.localizedMessage ?: "")
            }
        }
    }
}

sealed interface ImageUiState {
    data object Initial : ImageUiState
    data object Loading : ImageUiState
    data class Success(val outputText: String) : ImageUiState
    data class Error(val errorMessage: String) : ImageUiState
}