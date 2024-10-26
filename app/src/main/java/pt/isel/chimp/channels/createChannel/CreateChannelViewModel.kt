package pt.isel.chimp.channels.createChannel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.ChannelError
import pt.isel.chimp.service.ChannelService
import pt.isel.chimp.service.MockChannelService

sealed interface ChannelsListScreenState {
    data object Idle : ChannelsListScreenState
    data object Loading : ChannelsListScreenState
    data class Success(val channels: List<Channel>) : ChannelsListScreenState
    data class Error(val exception: ChannelError) : ChannelsListScreenState
}

class CreateChannelViewModel (private val channelService: ChannelService) : ViewModel(){
//todo
}

@Suppress("UNCHECKED_CAST")
class CreateChannelViewModelFactory(private val service: ChImpService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>):  T {
        return CreateChannelViewModel(service.channelService) as T
    }
}