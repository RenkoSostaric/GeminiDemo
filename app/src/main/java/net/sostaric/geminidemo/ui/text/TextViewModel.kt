package net.sostaric.geminidemo.ui.text

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TextViewModel(
    private val generativeModel: GenerativeModel
) : ViewModel() {

    private val _uiState: MutableStateFlow<TextUiState> = MutableStateFlow(TextUiState.Initial)
    val uiState: StateFlow<TextUiState> = _uiState.asStateFlow()

    fun generateText(inputText: String) {
        _uiState.value = TextUiState.Loading

        viewModelScope.launch {
            try {
                val response = generativeModel.generateContent(inputText)
                response.text?.let { outputContent ->
                    _uiState.value = TextUiState.Success(outputContent)
                }
            } catch (e: Exception) {
                _uiState.value = TextUiState.Error(e.localizedMessage ?: "")
            }
        }
    }
}

sealed interface TextUiState {
    data object Initial : TextUiState
    data object Loading : TextUiState
    data class Success(val outputText: String) : TextUiState
    data class Error(val errorMessage: String) : TextUiState
}