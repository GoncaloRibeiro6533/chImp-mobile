package pt.isel.chimp.channels.channel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.ChannelError
import pt.isel.chimp.service.ChannelService
import pt.isel.chimp.service.MessageService

sealed interface ChannelScreenState {
    data object Idle : ChannelScreenState
    data object Loading : ChannelScreenState
    data class Success(val channel: Channel) : ChannelScreenState
    data class Error(val exception: ChannelError) : ChannelScreenState

}

class ChannelViewModel(
    private val channelService: ChannelService,
    //private val messageService: MessageService
) : ViewModel() {

    var state: ChannelScreenState by mutableStateOf(ChannelScreenState.Idle)
        private set

    fun sendMessage() {
        TODO()
    }
}

@Suppress("UNCHECKED_CAST")
class ChannelViewModelFactory(
    private val service : ChImpService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChannelViewModel(service.channelService) as T
    }
}