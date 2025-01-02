package pt.isel.chimp.channels.channel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.message.Message
import pt.isel.chimp.domain.ApiError
import pt.isel.chimp.repository.ChImpRepo
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success

sealed interface ChannelScreenState {
    data object Uninitialized : ChannelScreenState
    data class LoadFromRemote(val channel: Channel) : ChannelScreenState
    data class SendingMessage (val messages: StateFlow<List<Message>>)  : ChannelScreenState
    data class LoadingMoreMessages(val channel: Channel) : ChannelScreenState
    data class LoadedAll(val messages: StateFlow<List<Message>>) : ChannelScreenState
    data class SaveData(val messages: List<Message>) : ChannelScreenState
    data class SaveMore(val messages: List<Message>) : ChannelScreenState
    data class Success(val messages: StateFlow<List<Message>>) : ChannelScreenState
    data class Error(val error: ApiError) : ChannelScreenState
}

const val MESSAGES_LIMIT = 40

class ChannelViewModel(
    private val repo : ChImpRepo,
    private val service: ChImpService,
    initialState: ChannelScreenState = ChannelScreenState.Uninitialized
) : ViewModel() {

    private val _state = MutableStateFlow<ChannelScreenState>(initialState)
    val state = _state.asStateFlow()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    private val _channel = MutableStateFlow<Channel?>(null)
    val channelF = _channel.asStateFlow()

    fun sendMessage(channel: Channel, content: String)  {
        if (_state.value is ChannelScreenState.Success
            || _state.value is ChannelScreenState.LoadedAll) {
            val state = _state.value
            _state.value = ChannelScreenState.SendingMessage(_messages)
            viewModelScope.launch {
                    _state.value = try {
                        val result = service.messageService.createMessage(channel.id, content)
                        when (result) {
                            is Success ->
                                if (state is ChannelScreenState.LoadedAll) ChannelScreenState.LoadedAll(_messages)
                                else ChannelScreenState.Success(_messages)
                            is Failure -> ChannelScreenState.Error(result.value)
                        }
                    } catch (e: Throwable) {
                        ChannelScreenState.Error(ApiError("Error sending message"))
                    }
                }
        }
    }


    fun loadLocalData(channel: Channel): Job? {
        if (_state.value !is ChannelScreenState.Uninitialized) return null
        return viewModelScope.launch {
            try {
                repo.messageRepo.getMessages(channel).collect { stream ->
                    _messages.value = stream
                    if (stream.isNotEmpty() &&
                        (_state.value !is ChannelScreenState.SaveData ||
                                _state.value !is ChannelScreenState.SaveMore ||
                                _state.value !is ChannelScreenState.LoadedAll ||
                                _state.value !is ChannelScreenState.Success
                            )) {
                        if (_messages.value.size >= MESSAGES_LIMIT) _state.value = ChannelScreenState.Success(_messages)
                        else _state.value = ChannelScreenState.LoadedAll(_messages)
                    }
                    if (stream.isEmpty() && _state.value is ChannelScreenState.Uninitialized){
                        _state.value = ChannelScreenState.LoadFromRemote(channel)
                    }
                }
            } catch (e: Throwable) {
                _state.value = ChannelScreenState.Error(ApiError("Error getting channels: ${e.message}"))
            }
        }
    }

    fun loadRemoteData(channel: Channel, limit: Int = MESSAGES_LIMIT, skip: Int = 0) {
        if (_state.value is ChannelScreenState.LoadFromRemote || _state.value is ChannelScreenState.Success) {
            if (_state.value is ChannelScreenState.Success)
                _state.value = ChannelScreenState.LoadingMoreMessages(channel)
            viewModelScope.launch {
                try {
                    when (val result =service.messageService.getMessagesByChannel(channel.id, limit, skip)) {
                        is Success -> {
                            if (result.value.isEmpty()) {
                                _state.value = ChannelScreenState.LoadedAll(_messages)
                                return@launch
                            }
                            _state.value = if (_state.value is ChannelScreenState.LoadFromRemote)
                                ChannelScreenState.SaveData(result.value)
                            else ChannelScreenState.SaveMore(result.value)
                        }
                        is Failure -> {
                            _state.value = ChannelScreenState.Error(result.value)
                        }
                    }
                }catch (e: Throwable) {
                    _state.value =
                        ChannelScreenState.Error(ApiError("Error getting channels: ${e.message}"))
                }
            }
        }
    }

    fun saveData(messages: List<Message>) {
        if (_state.value is ChannelScreenState.SaveData || _state.value is ChannelScreenState.SaveMore) {
            viewModelScope.launch {
                try {
                    repo.messageRepo.insertMessage(messages)
                    _state.value = if (messages.size == MESSAGES_LIMIT)ChannelScreenState.Success(_messages)
                    else ChannelScreenState.LoadedAll(_messages)
                } catch (e: Throwable) {
                    _state.value = ChannelScreenState.Error(ApiError("Error saving messages"))
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