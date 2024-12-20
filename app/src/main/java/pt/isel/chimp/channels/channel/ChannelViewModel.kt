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
import pt.isel.chimp.repository.ChImpRepo
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success

sealed interface ChannelScreenState {
    data object Initialized : ChannelScreenState
    data object Idle : ChannelScreenState
    data object Loading : ChannelScreenState
    data class Success(val messages: List<Message>) : ChannelScreenState
    data class Error(val error: ApiError) : ChannelScreenState
}

class ChannelViewModel(
    private val repo : ChImpRepo,
    private val service: ChImpService,
    initialState: ChannelScreenState = ChannelScreenState.Initialized
) : ViewModel() {

    private val _state = MutableStateFlow<ChannelScreenState>(initialState)
    val state = _state.asStateFlow()



    fun sendMessage(channel: Channel, content: String)  {
        if (_state.value != ChannelScreenState.Loading) {
            _state.value = ChannelScreenState.Loading
            viewModelScope.launch {
                repo.messageRepo.getMessages(channel).collect { messages ->
                    _state.value = try {
                        val result = service.messageService.createMessage(channel.id, content)
                        when (result) {
                            is Success -> ChannelScreenState.Success(messages)
                            is Failure -> ChannelScreenState.Error(result.value)
                        }
                    } catch (e: Throwable) {
                        ChannelScreenState.Error(ApiError("Error sending message"))
                    } as ChannelScreenState
                }
            }
        }
    }


    fun getMessages(channel: Channel, limit: Int, skip: Int) {
        //TODO if local data is available, return it and do not set _state as loading
        if (_state.value != ChannelScreenState.Loading) {
            _state.value = ChannelScreenState.Loading
            viewModelScope.launch {
                repo.messageRepo.getMessages(channel).collect { messages ->
                    if (messages.isNotEmpty()) {
                        _state.value = ChannelScreenState.Success(messages)
                        return@collect
                    }
                try {
                    val messages = service.messageService.getMessagesByChannel(channel.id, limit, skip)
                    when (messages) {
                        is Success -> {
                            repo.messageRepo.insertMessage(messages.value)
                            _state.value = ChannelScreenState.Success(messages.value)
                        }
                        is Failure -> _state.value = ChannelScreenState.Error(messages.value)
                    }
                } catch (e: Throwable) {
                    _state.value = ChannelScreenState.Error(ApiError("Error getting messages"))
                }
                }
            }
        }
    }

}

@Suppress("UNCHECKED_CAST")
class ChannelViewModelFactory(
    private val repo: ChImpRepo,
    private val service : ChImpService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChannelViewModel(
            repo,
            service
        ) as T
    }
}