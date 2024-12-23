package pt.isel.chimp.channels.searchChannels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.repository.UserInfoRepository
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.ChannelService
import pt.isel.chimp.service.UserService
import pt.isel.chimp.utils.Either
import pt.isel.chimp.utils.Success
import pt.isel.chimp.utils.Failure

sealed interface SearchChannelsScreenState {
    //data object Idle : SearchChannelsScreenState
    data object Loading : SearchChannelsScreenState
    data object Typing : SearchChannelsScreenState
    data class Success(val channels: List<Channel>) : SearchChannelsScreenState
    data class Error(val error: ApiError) : SearchChannelsScreenState
}

class SearchChannelsViewModel(
    private val channelService: ChannelService,
    private val userService: UserService,
    initialState: SearchChannelsScreenState = SearchChannelsScreenState.Typing
) : ViewModel() {

    private val _state = MutableStateFlow<SearchChannelsScreenState>(initialState)
    val state: StateFlow<SearchChannelsScreenState> = _state.asStateFlow()

    private val _userChannels = MutableStateFlow<List<Channel>>(emptyList())
    val userChannels: StateFlow<List<Channel>> = _userChannels.asStateFlow()


    fun getChannels(name :String, limit: Int, skip: Int) {

            viewModelScope.launch {
                if (name.isEmpty()) {
                    _state.value = SearchChannelsScreenState.Typing
                } else {
                    _state.value = try {
                        when (val channels = channelService.searchChannelByName(name, limit, skip)) {
                            is Success -> SearchChannelsScreenState.Success(channels.value)
                            is Failure -> SearchChannelsScreenState.Error(channels.value)
                        }
                    } catch (e: Throwable) {
                        SearchChannelsScreenState.Error(ApiError("Error searching channels"))
                    }
            }
        }
    }
    fun getUsersOfChannel(userId: Int) {
        viewModelScope.launch {
            _userChannels.value = try {
                when (val result = channelService.getChannelsOfUser(userId)) {
                    is Either.Left -> {
                        emptyList()
                    }
                    is Either.Right -> {
                        result.value.keys.toList()
                    }
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }


    fun addUserToChannel(userId: Int, channelId: Int) {
        viewModelScope.launch {
            try {
                channelService.joinChannel(userId, channelId, Role.READ_WRITE)
                channelService.getChannelsOfUser(userId)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class SearchChannelsViewModelFactory(
    private val repo: UserInfoRepository,
    private val service: ChImpService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchChannelsViewModel(service.channelService, service.userService) as T
    }
}
