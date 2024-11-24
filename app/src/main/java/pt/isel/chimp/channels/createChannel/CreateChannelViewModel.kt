package pt.isel.chimp.channels.createChannel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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

class CreateChannelViewModel (private val channelService: ChannelService) : ViewModel(){

    var state: CreateChannelScreenState by mutableStateOf(CreateChannelScreenState.Idle)
        private set


    fun createChannel(name: String, creatorId: Int, visibility: String, token: String) {
        if (state != CreateChannelScreenState.Loading) {
            state = CreateChannelScreenState.Loading
            viewModelScope.launch {
                state = try {
                    val channel = channelService.createChannel(
                        name,
                        creatorId,
                        Visibility.valueOf(visibility),
                        token
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
        state = CreateChannelScreenState.Idle
    }
}

@Suppress("UNCHECKED_CAST")
class CreateChannelViewModelFactory(private val service: ChImpService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>):  T {
        return CreateChannelViewModel(service.channelService) as T
    }
}