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
import pt.isel.chimp.domain.user.AuthenticatedUser
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.ChannelService
import pt.isel.chimp.service.MessageService
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success

sealed interface ChannelScreenState {
    data object Idle : ChannelScreenState
    data object Loading : ChannelScreenState
    data class Success(val messages: List<Message>) : ChannelScreenState
    data class Error(val error: ApiError) : ChannelScreenState
}

class ChannelViewModel(
    private val channelService: ChannelService,
    private val messageService: MessageService
) : ViewModel() {

    var state: ChannelScreenState by mutableStateOf(ChannelScreenState.Idle)
        private set

    fun findChannelById(id: Int, token: String) {
        if (state != ChannelScreenState.Loading) {
            state = ChannelScreenState.Loading
            viewModelScope.launch {
                state = try {
                    val channel = channelService.getChannelById(id, token)
                    when (channel) {
                        is Success -> ChannelScreenState.Success(listOf())
                        is Failure -> ChannelScreenState.Error(channel.value)
                    }
                } catch (e: Throwable) {
                    ChannelScreenState.Error(ApiError("Error finding channel"))
                }
            }
        }
    }


    fun sendMessage(channel: Channel, user: AuthenticatedUser, content: String, token: String)  {
        if (state != ChannelScreenState.Loading) {
            state = ChannelScreenState.Loading
            viewModelScope.launch {
                state = try {
                    val messages = messageService.createMessage(token, user.user.id, channel.id, content)
                    when (messages) {
                        is Success -> ChannelScreenState.Success(listOf(messages.value))
                        is Failure -> ChannelScreenState.Error(messages.value)
                    }
                } catch (e: Throwable) {
                    ChannelScreenState.Error(ApiError("Error sending message"))
                }
            }
        }
    }


    fun getMessages(channelId: Int, limit: Int, skip: Int, token: String) {
        if (state != ChannelScreenState.Loading) {
            state = ChannelScreenState.Loading
            viewModelScope.launch {
                state = try {
                    val messages = messageService.getMessagesByChannel(token, channelId, limit, skip)
                    when (messages) {
                        is Success -> ChannelScreenState.Success(messages.value)
                        is Failure -> ChannelScreenState.Error(messages.value)
                    }
                } catch (e: Throwable) {
                    ChannelScreenState.Error(ApiError("Error getting messages"))
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