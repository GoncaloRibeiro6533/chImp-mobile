package pt.isel.chimp.channels.channelsList

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
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.domain.ApiError
import pt.isel.chimp.service.http.utils.ChImpException
import pt.isel.chimp.repository.ChImpRepo
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.ChannelService
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success

sealed interface ChannelsListScreenState {
    data object Uninitialized : ChannelsListScreenState
    data object LoadFromRemote: ChannelsListScreenState
    data class SaveData(val user: User, val channels: Map<Channel,Role>) : ChannelsListScreenState
    data class Success(val channels: StateFlow<Map<Channel,Role>>) : ChannelsListScreenState
    data class Error(val error: ApiError) : ChannelsListScreenState
}

class ChannelsListViewModel(
    private val userInfo: UserInfoRepository,
    private val channelService: ChannelService,
    private val repo: ChImpRepo,
    initialState: ChannelsListScreenState = ChannelsListScreenState.Uninitialized
) : ViewModel() {

    private val _state = MutableStateFlow<ChannelsListScreenState>(initialState)
    val state: StateFlow<ChannelsListScreenState> = _state.asStateFlow()

    private val _channels = MutableStateFlow<Map<Channel, Role>>(emptyMap())
    val channels: StateFlow<Map<Channel, Role>> = _channels.asStateFlow()


    fun onFatalError() {
        viewModelScope.launch {
            try {
                repo.invitationRepo.deleteAllInvitations()
                repo.messageRepo.clear()
                repo.channelRepo.clear()
                repo.userRepo.clear()
                userInfo.clearUserInfo()
            } catch (e: Exception) {
                userInfo.clearUserInfo()
                repo.invitationRepo.deleteAllInvitations()
                repo.messageRepo.clear()
                repo.channelRepo.clear()
                repo.userRepo.clear()
            }
        }
    }


    fun loadLocalData(): Job? {
        if (_state.value !is ChannelsListScreenState.Uninitialized) return null
        return viewModelScope.launch {
            try {
                repo.channelRepo.getChannels().collect { stream ->
                    _channels.value = stream
                    if (stream.isNotEmpty() && _state.value !is ChannelsListScreenState.Success) {
                        _state.value = ChannelsListScreenState.Success(_channels)
                    }
                    if (stream.isEmpty() && _state.value is ChannelsListScreenState.Uninitialized) {
                        _state.value = ChannelsListScreenState.LoadFromRemote
                    }
                }
            } catch (e: Throwable) {
                _state.value =
                    ChannelsListScreenState.Error(ApiError("Error getting channels: ${e.message}"))
            }
        }
    }


    fun loadRemoteData() {
        if (_state.value is ChannelsListScreenState.LoadFromRemote) {
            viewModelScope.launch {
                try {
                    val user =
                        userInfo.getUserInfo() ?: throw ChImpException("User not found", null)
                    when (val result = channelService.getChannelsOfUser(user.id)) {
                        is Success -> _state.value =
                            ChannelsListScreenState.SaveData(user, result.value)

                        is Failure -> {
                            _state.value = ChannelsListScreenState.Error(result.value)
                        }
                    }
                } catch (e: Throwable) {
                    _state.value =
                        ChannelsListScreenState.Error(ApiError("Error getting channels: ${e.message}"))
                }
            }
        }
    }

    fun saveData(user: User, channelsMap: Map<Channel, Role>) {
        if (_state.value is ChannelsListScreenState.SaveData) {
            viewModelScope.launch {
                try {
                    repo.channelRepo.insertChannels(user.id, channelsMap)
                    _channels.value = channelsMap
                    _state.value = ChannelsListScreenState.Success(channels)
                } catch (e: Throwable) {
                    _state.value =
                        ChannelsListScreenState.Error(ApiError("Error saving channels: ${e.message}"))
                }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class ChannelsListViewModelFactory(
    private val userInfo: UserInfoRepository,
    private val service: ChImpService,
    private val repo: ChImpRepo
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>):  T {
        return ChannelsListViewModel(
            userInfo,
            service.channelService,
            repo
        ) as T
    }
}