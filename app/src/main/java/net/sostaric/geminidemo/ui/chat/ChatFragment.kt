package net.sostaric.geminidemo.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.Surface
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import net.sostaric.geminidemo.GenerativeAiViewModelFactory

class ChatFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val chatViewModel = ViewModelProvider(this, GenerativeAiViewModelFactory)
            .get(ChatViewModel::class.java)

        return ComposeView(requireContext()).apply {
            setContent {
                Surface {
                    ChatRoute(chatViewModel)
                }
            }
        }
    }
}