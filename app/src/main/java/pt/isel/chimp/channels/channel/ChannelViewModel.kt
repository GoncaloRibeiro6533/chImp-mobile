package pt.isel.chimp.channels.channel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.message.Message
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.ChannelError
import pt.isel.chimp.service.ChannelService
import pt.isel.chimp.service.MessageError
import pt.isel.chimp.service.MessageService
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success

sealed interface ChannelScreenState {
    data object Idle : ChannelScreenState
    data object Loading : ChannelScreenState
    data class Success(val messages: List<Message>) : ChannelScreenState
    data class MsgError(val exception: MessageError) : ChannelScreenState
    data class ChError(val exception: ChannelError) : ChannelScreenState
}

class ChannelViewModel(
    private val channelService: ChannelService,
    private val messageService: MessageService
) : ViewModel() {

    var state: ChannelScreenState by mutableStateOf(ChannelScreenState.Idle)
        private set

    fun findChannelById(id: Int, user: User) {
        if (state != ChannelScreenState.Loading) {
            state = ChannelScreenState.Loading
            viewModelScope.launch {
                val channel = channelService.getChannelById(id, user)
                state = when (channel) {
                    is Success -> ChannelScreenState.Success(listOf())
                    is Failure -> ChannelScreenState.ChError(channel.value)
                }
            }
        }
    }


    fun sendMessage(user: User, channel: Channel, content: String) {
        if (state != ChannelScreenState.Loading) {
            state = ChannelScreenState.Loading
            viewModelScope.launch {
                val messages = messageService.createMessage(user.id, channel.id, content)
                state = when (messages) {
                    is Success -> ChannelScreenState.Success(listOf(messages.value))
                    is Failure -> ChannelScreenState.MsgError(messages.value)
                }
            }
        }
    }

    fun getMessages(channelId: Int, limit: Int, skip: Int) {
        if (state != ChannelScreenState.Loading) {
            state = ChannelScreenState.Loading
            viewModelScope.launch {
                val messages = messageService.getMessagesByChannel(channelId, limit, skip)
                state = when (messages) {
                    is Success -> ChannelScreenState.Success(messages.value)
                    is Failure -> ChannelScreenState.MsgError(messages.value)
                }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class ChannelViewModelFactory(
    private val service : ChImpService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChannelViewModel(service.channelService, service.messageService) as T
    }
}