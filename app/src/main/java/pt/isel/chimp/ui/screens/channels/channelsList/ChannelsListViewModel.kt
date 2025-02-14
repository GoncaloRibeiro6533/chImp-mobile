package pt.isel.chimp.ui.screens.channels.channelsList

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
import pt.isel.chimp.service.http.utils.ChImpException
import pt.isel.chimp.repository.ChImpRepo

sealed interface ChannelsListScreenState {
    data object Uninitialized : ChannelsListScreenState
    data object Loading: ChannelsListScreenState
    data class Success(val channels: StateFlow<Map<Channel,Role>>) : ChannelsListScreenState
    data class Error(val error: ApiError) : ChannelsListScreenState
}

class ChannelsListViewModel(
    private val userInfo: UserInfoRepository,
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
        if (_state.value is ChannelsListScreenState.Loading) return null
        _state.value = ChannelsListScreenState.Loading
        return viewModelScope.launch {
            try {
                val user = userInfo.getUserInfo() ?: throw ChImpException("User not found", null)
                repo.channelRepo.fetchChannels(user, 100, 0).collect { stream ->
                    _channels.value = stream
                    _state.value = ChannelsListScreenState.Success(channels)
                }
            } catch (e: Throwable) {
                _state.value =
                    ChannelsListScreenState.Error(ApiError("Error getting channels: ${e.message}"))
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class ChannelsListViewModelFactory(
    private val userInfo: UserInfoRepository,
    private val repo: ChImpRepo
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>):  T {
        return ChannelsListViewModel(
            userInfo,
            repo
        ) as T
    }
}