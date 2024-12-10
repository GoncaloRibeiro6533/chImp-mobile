package pt.isel.chimp.channels.channel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.message.Message
import pt.isel.chimp.domain.repository.UserInfoRepository
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success

sealed interface ChannelScreenState {
    data object Initialized : ChannelScreenState
    data object Idle : ChannelScreenState
    data object Loading : ChannelScreenState
   // data class SuccessOnFindChannel(val channel: Channel) : ChannelScreenState
    data class SuccessOnSendMessage(val messages: Message) : ChannelScreenState
    data class Success(val messages: List<Message>) : ChannelScreenState
    data class Error(val error: ApiError) : ChannelScreenState
}

class ChannelViewModel(
    private val repo : UserInfoRepository,
    private val service: ChImpService,
    initialState: ChannelScreenState = ChannelScreenState.Initialized
) : ViewModel() {

    private val _state = MutableStateFlow<ChannelScreenState>(initialState)
    val state = _state.asStateFlow()



    fun sendMessage(channel: Channel, content: String)  {
        if (_state.value != ChannelScreenState.Loading) {
            _state.value = ChannelScreenState.Loading
            viewModelScope.launch {
                _state.value = try {
                    val messages = service.messageService.createMessage(channel.id, content)
                    when (messages) {
                        is Success -> ChannelScreenState.SuccessOnSendMessage(messages.value)
                        is Failure -> ChannelScreenState.Error(messages.value)
                    }
                } catch (e: Throwable) {
                    ChannelScreenState.Error(ApiError("Error sending message"))
                }
            }
        }
    }


    fun getMessages(channelId: Int, limit: Int, skip: Int) {
        //TODO if local data is available, return it and do not set _state as loading
        if (_state.value != ChannelScreenState.Loading) {
            _state.value = ChannelScreenState.Loading
            viewModelScope.launch {
                _state.value = try {
                    val messages = service.messageService.getMessagesByChannel(channelId, limit, skip)
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
    private val repo: UserInfoRepository,
    private val service : ChImpService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChannelViewModel(
            repo,
            service
        ) as T
    }
}