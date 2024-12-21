package pt.isel.chimp.channels.channel

import android.util.Log
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
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.repository.ChImpRepo
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success

sealed interface ChannelScreenState {
    data object Uninitialized : ChannelScreenState
    data class LoadFromRemote(val channel: Channel) : ChannelScreenState
  //  data object Loading : ChannelScreenState
    data class SaveData(val messages: List<Message>) : ChannelScreenState
    data class Success(val messages: StateFlow<List<Message>>) : ChannelScreenState
    data class Error(val error: ApiError) : ChannelScreenState
}

class ChannelViewModel(
    private val repo : ChImpRepo,
    private val service: ChImpService,
    initialState: ChannelScreenState = ChannelScreenState.Uninitialized
) : ViewModel() {

    private val _state = MutableStateFlow<ChannelScreenState>(initialState)
    val state = _state.asStateFlow()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()
    //private val _messages = MutableSharedFlow<List<Message>>(replay = 1)
    //private val messages = _messages.asSharedFlow()



    fun sendMessage(channel: Channel, content: String)  {
        if (_state.value is ChannelScreenState.Success) {
            //_state.value = ChannelScreenState.Loading
            viewModelScope.launch {
                    _state.value = try {
                        val result = service.messageService.createMessage(channel.id, content)
                        when (result) {
                            is Success -> ChannelScreenState.Success(messages)
                            is Failure -> ChannelScreenState.Error(result.value)
                        }
                    } catch (e: Throwable) {
                        ChannelScreenState.Error(ApiError("Error sending message"))
                    }
                }
        }
    }


    /*fun getMessages(channel: Channel, limit: Int, skip: Int) {
        //TODO if local data is available, return it and do not set _state as loading
        if (_state.value != ChannelScreenState.Loading) {
            _state.value = ChannelScreenState.Loading
            viewModelScope.launch {
                repo.messageRepo.getMessages(channel).collect { stream ->
                    if (stream.isNotEmpty()) {
                        _state.value = ChannelScreenState.Success(messages)
                        return@collect
                    }
                try {
                    Log.d("ChannelViewModel", "Getting messages")
                    val result = service.messageService.getMessagesByChannel(channel.id, limit, skip)
                    when (result) {
                        is Success -> {
                            repo.messageRepo.insertMessage(result.value)
                            _messages.emit(stream)
                            if (stream.isNotEmpty())_state.value = ChannelScreenState.Success(messages)
                            else _state.value = ChannelScreenState.Loading
                        }
                        is Failure -> _state.value = ChannelScreenState.Error(result.value)
                    }
                } catch (e: Throwable) {
                    _state.value = ChannelScreenState.Error(ApiError("Error getting messages"))
                }
                }
            }
        }
    }*/

    fun loadLocalData(channel: Channel): Job? {
        if (_state.value !is ChannelScreenState.Uninitialized) return null
        return viewModelScope.launch {
            try {
                repo.messageRepo.getMessages(channel).collect { stream ->
                    Log.d("ChannelViewModel", stream.toString())
                    _messages.value = stream
                    if (stream.isNotEmpty()) {
                        _state.value = ChannelScreenState.Success(messages)
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

    fun loadRemoteData(channel: Channel) {
        if (_state.value is ChannelScreenState.LoadFromRemote) {
            viewModelScope.launch {
                try {
                    when (val result =service.messageService.getMessagesByChannel(channel.id, 40, 0)) {
                        is Success -> _state.value = ChannelScreenState.SaveData(result.value)
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
        if (_state.value is ChannelScreenState.SaveData) {
            viewModelScope.launch {
                try {
                    repo.messageRepo.insertMessage(messages)
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