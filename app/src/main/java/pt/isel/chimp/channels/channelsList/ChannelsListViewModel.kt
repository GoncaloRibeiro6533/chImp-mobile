package pt.isel.chimp.channels.channelsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.repository.UserInfoRepository
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.http.utils.ChImpException
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.ChannelService
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success

sealed interface ChannelsListScreenState {
    data object Initialized : ChannelsListScreenState
    data object Idle : ChannelsListScreenState
    data object Loading : ChannelsListScreenState
    data class Success(val channels: Map<Channel,Role>) : ChannelsListScreenState
    data class Error(val error: ApiError) : ChannelsListScreenState
}

class ChannelsListViewModel(
    private val repo: UserInfoRepository,
    private val channelService: ChannelService,
    initialState: ChannelsListScreenState = ChannelsListScreenState.Initialized
) : ViewModel() {

    private val _state = MutableStateFlow<ChannelsListScreenState>(initialState)
    val state = _state.asStateFlow()

    fun getChannels() {
        //TODO if is not initialized get from local storage else fetch from server
        if (_state.value != ChannelsListScreenState.Loading) {
            _state.value = ChannelsListScreenState.Loading
            viewModelScope.launch {
                _state.value = try {
                    val user = repo.getUserInfo() ?: throw ChImpException(message = "User not logged in", null)
                    when (val channels = channelService.getChannelsOfUser(user.id)) {
                        is Success -> ChannelsListScreenState.Success(channels.value)
                        is Failure -> ChannelsListScreenState.Error(channels.value)
                    }
                } catch (e: Throwable) {
                    ChannelsListScreenState.Error(ApiError("Error getting channels"))
                }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class ChannelsListViewModelFactory(
    private val repo: UserInfoRepository,
    private val service: ChImpService
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>):  T {
        return ChannelsListViewModel(
            repo,
            service.channelService) as T
    }
}