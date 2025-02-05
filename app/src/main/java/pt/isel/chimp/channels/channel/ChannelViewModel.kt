package pt.isel.chimp.channels.channel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.chimp.domain.ApiError
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.message.Message
import pt.isel.chimp.repository.ChImpRepo
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success

sealed interface ChannelScreenState {
    data object Uninitialized : ChannelScreenState
    data class Loading(val channel: Channel) : ChannelScreenState
    data class SendingMessage(val messages: StateFlow<List<Message>>) : ChannelScreenState
    data class LoadingMoreMessages(val channel: Channel) : ChannelScreenState
    data class LoadedAll(val messages: StateFlow<List<Message>>) : ChannelScreenState
    data class Success(val messages: StateFlow<List<Message>>) : ChannelScreenState
    data class Error(val error: ApiError) : ChannelScreenState
}

const val MESSAGES_LIMIT = 40

class ChannelViewModel(
    private val repo: ChImpRepo,
    private val service: ChImpService,
    initialState: ChannelScreenState = ChannelScreenState.Uninitialized
) : ViewModel() {

    private val _state = MutableStateFlow<ChannelScreenState>(initialState)
    val state = _state.asStateFlow()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()


    fun sendMessage(channel: Channel, content: String) {
        if (_state.value is ChannelScreenState.Success
            || _state.value is ChannelScreenState.LoadedAll
        ) {
            val state = _state.value
            _state.value = ChannelScreenState.SendingMessage(_messages)
            viewModelScope.launch {
                _state.value = try {
                    val result = service.messageService.createMessage(channel.id, content)
                    when (result) {
                        is Success -> {
                            if (state is ChannelScreenState.LoadedAll) ChannelScreenState.LoadedAll(
                                _messages
                            )
                            else ChannelScreenState.Success(_messages)
                        }

                        is Failure -> ChannelScreenState.Error(result.value)
                    }
                } catch (e: Throwable) {
                    ChannelScreenState.Error(ApiError("Error sending message"))
                }
            }
        }
    }


    fun loadMessages(channel: Channel): Job? {
        if (_state.value !is ChannelScreenState.Uninitialized) return null
        _state.value = ChannelScreenState.Loading(channel)
        return viewModelScope.launch {
            try {
                repo.messageRepo.fetchMessages(channel, MESSAGES_LIMIT, 0).collect { stream ->
                    _messages.value = stream
                    if (_state.value is ChannelScreenState.Loading) {
                        if (_messages.value.size >= MESSAGES_LIMIT) _state.value =
                            ChannelScreenState.Success(_messages)
                        else _state.value = ChannelScreenState.LoadedAll(_messages)
                    }
                }
            } catch (e: Throwable) {
                _state.value =
                    ChannelScreenState.Error(ApiError("Error getting channels: ${e.message}"))
            }
        }
    }

    fun loadMoreMessages(channel: Channel, limit: Int = MESSAGES_LIMIT, skip: Int = 0) {
        if (_state.value is ChannelScreenState.Success) {
            _state.value = ChannelScreenState.LoadingMoreMessages(channel)
            viewModelScope.launch {
                try {
                    _state.value = when (val result =
                        repo.messageRepo.loadMoreMessages(channel, limit, skip)) {
                        is Success -> {
                            _messages.value = _messages.value + result.value
                            if (result.value.size < limit) {
                                ChannelScreenState.LoadedAll(_messages)
                            } else {
                                ChannelScreenState.Success(_messages)
                            }
                        }
                        is Failure -> {
                            ChannelScreenState.Error(result.value)
                        }
                    }
                } catch (e: Throwable) {
                    _state.value =
                        ChannelScreenState.Error(ApiError("Error getting channels: ${e.message}"))
                }
            }
        }
    }
}


@Suppress("UNCHECKED_CAST")
class ChannelViewModelFactory(
    private val repo: ChImpRepo,
    private val service: ChImpService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChannelViewModel(
            repo,
            service
        ) as T
    }
}