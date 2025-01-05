package net.sostaric.geminidemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import net.sostaric.geminidemo.ui.text.TextViewModel
import net.sostaric.geminidemo.ui.chat.ChatViewModel
import net.sostaric.geminidemo.ui.image.ImageViewModel

val GenerativeAiViewModelFactory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        viewModelClass: Class<T>,
        extras: CreationExtras
    ): T {
        val config = generationConfig {
            temperature = 0.7f
        }

        return with(viewModelClass) {
            when {
                isAssignableFrom(TextViewModel::class.java) -> {
                    val generativeModel = GenerativeModel(
                        modelName = "gemini-1.5-flash-latest",
                        apiKey = BuildConfig.apiKey,
                        generationConfig = config
                    )
                    TextViewModel(generativeModel)
                }

/*                 isAssignableFrom(ImageViewModel::class.java) -> {
                    val generativeModel = GenerativeModel(
                        modelName = "gemini-1.5-flash-latest",
                        apiKey = BuildConfig.apiKey,
                        generationConfig = config
                    )
                    ImageViewModel(generativeModel)
                }

                isAssignableFrom(ChatViewModel::class.java) -> {
                    val generativeModel = GenerativeModel(
                        modelName = "gemini-1.5-flash-latest",
                        apiKey = BuildConfig.apiKey,
                        generationConfig = config
                    )
                    ChatViewModel(generativeModel)
                }
 */
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${viewModelClass.name}")
            }
        } as T
    }
} 