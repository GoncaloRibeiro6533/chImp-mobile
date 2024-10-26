package pt.isel.chimp.channels.channelsList

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.ChannelError
import pt.isel.chimp.service.ChannelService
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success

sealed interface ChannelsListScreenState {
    data object Idle : ChannelsListScreenState
    data object Loading : ChannelsListScreenState
    data class Success(val channels: List<Channel>) : ChannelsListScreenState
    data class Error(val exception: ChannelError) : ChannelsListScreenState
}

class ChannelsListViewModel(private val channelService: ChannelService) : ViewModel() {
    var state: ChannelsListScreenState by mutableStateOf(ChannelsListScreenState.Idle)
        private set

    fun getChannels(user: User) { //todo search by user or by id?
        if (state != ChannelsListScreenState.Loading) {
            state = ChannelsListScreenState.Loading
            viewModelScope.launch {
                val channels = channelService.getChannelsByUser(user)
                state = when (channels) {
                    is Success -> ChannelsListScreenState.Success(channels.value)
                    is Failure -> ChannelsListScreenState.Error(channels.value)

                }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class ChannelsListViewModelFactory(private val service: ChImpService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>):  T {
        return ChannelsListViewModel(service.channelService) as T
    }
}