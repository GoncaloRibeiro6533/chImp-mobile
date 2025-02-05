package pt.isel.chimp.channels.searchChannels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.repository.UserInfoRepository
import pt.isel.chimp.domain.ApiError
import pt.isel.chimp.repository.ChImpRepo
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success

sealed interface SearchChannelsScreenState {
    data object Loading : SearchChannelsScreenState
    data object Typing : SearchChannelsScreenState
    data class EnteringChannel(val channel: Channel, val role: Role) : SearchChannelsScreenState
    data class Success(val channels: List<Channel>, val channelsOfUser: StateFlow<Map<Channel,Role>>) : SearchChannelsScreenState
    data class Error(val error: ApiError) : SearchChannelsScreenState
}

class SearchChannelsViewModel(
    private val userInfoRepository: UserInfoRepository,
    private val repository: ChImpRepo,
    initialState: SearchChannelsScreenState = SearchChannelsScreenState.Typing
) : ViewModel() {

    private val _state = MutableStateFlow<SearchChannelsScreenState>(initialState)
    val state: StateFlow<SearchChannelsScreenState> = _state.asStateFlow()

    private val _channels = MutableStateFlow<Map<Channel, Role>>(emptyMap())
    val channels: StateFlow<Map<Channel, Role>> = _channels.asStateFlow()



    fun loadChannels(): Job? {
        return viewModelScope.launch {
            try {
                val user = userInfoRepository.getUserInfo() ?: throw Exception("User not found")
                repository.channelRepo.fetchChannels(user, 100, 0).collect { stream ->
                    _channels.value = stream
                }
            } catch (e: Throwable) {
                _state.value = SearchChannelsScreenState.Error(ApiError("Error getting channels"))
            }
        }
    }

    fun getChannels(name :String, limit: Int, skip: Int) {
        if (_state.value is SearchChannelsScreenState.Typing || _state.value is SearchChannelsScreenState.Success) {
            _state.value = SearchChannelsScreenState.Loading
            viewModelScope.launch {
                if (name.isEmpty()) {
                    _state.value = SearchChannelsScreenState.Typing
                } else {
                    _state.value = try {
                        when (val channels = repository.channelRepo.findByName(name, limit, skip)) {
                            is Success -> SearchChannelsScreenState.Success(channels.value, _channels)
                            is Failure -> SearchChannelsScreenState.Error(channels.value)
                        }
                    } catch (e: Throwable) {
                        SearchChannelsScreenState.Error(ApiError("Error searching channels"))
                    }
                }
            }
        }
    }

    fun addUserToChannel(channel: Channel) {
        viewModelScope.launch {
            try {
                val user =  userInfoRepository.getUserInfo() ?: throw Exception("User not found")
                val role = _channels.value[channel]
                if (role != null) {
                    _state.value = SearchChannelsScreenState.EnteringChannel(channel, role)
                    return@launch
                }
                repository.channelRepo.joinChannel(user, channel, Role.READ_WRITE)
                _state.value = SearchChannelsScreenState.EnteringChannel(channel, Role.READ_WRITE)
            } catch (e: Exception) {
                _state.value = SearchChannelsScreenState.Error(ApiError("Error adding user to channel"))
            }
        }
    }

}

@Suppress("UNCHECKED_CAST")
class SearchChannelsViewModelFactory(
    private val userInfoRepository: UserInfoRepository,
    private val repository: ChImpRepo,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchChannelsViewModel(userInfoRepository, repository) as T
    }
}
