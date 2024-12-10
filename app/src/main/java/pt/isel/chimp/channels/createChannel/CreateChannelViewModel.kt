package pt.isel.chimp.channels.createChannel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.ChannelService
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success

sealed interface CreateChannelScreenState {
    data object Idle : CreateChannelScreenState
    data object Loading : CreateChannelScreenState
    data class Success(val channel: Channel) : CreateChannelScreenState
    data class Error(val error: ApiError) : CreateChannelScreenState
}

class CreateChannelViewModel (
    private val channelService: ChannelService,
    initialState: CreateChannelScreenState = CreateChannelScreenState.Idle
    ) : ViewModel(){

    private val _state = MutableStateFlow<CreateChannelScreenState>(initialState)
    val state = _state.asStateFlow()


    fun createChannel(name: String, creatorId: Int, visibility: String) {
        if (_state.value != CreateChannelScreenState.Loading) {
            _state.value = CreateChannelScreenState.Loading
            viewModelScope.launch {
                _state.value = try {
                    val channel = channelService.createChannel(
                        name,
                        creatorId,
                        Visibility.valueOf(visibility)
                    )
                    when (channel) {
                        is Success -> CreateChannelScreenState.Success(channel.value)
                        is Failure -> CreateChannelScreenState.Error(channel.value)
                    }
                } catch (e: Throwable) {
                    CreateChannelScreenState.Error(ApiError("Error creating channel"))
                }
            }

        }
    }

    fun setIdleState() {
        _state.value = CreateChannelScreenState.Idle
    }
}

@Suppress("UNCHECKED_CAST")
class CreateChannelViewModelFactory(private val service: ChImpService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>):  T {
        return CreateChannelViewModel(service.channelService) as T
    }
}