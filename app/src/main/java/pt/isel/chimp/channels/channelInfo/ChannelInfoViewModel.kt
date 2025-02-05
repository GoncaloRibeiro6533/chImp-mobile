package pt.isel.chimp.channels.channelInfo

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
import pt.isel.chimp.repository.ChImpRepo
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success

data class ChannelInfo(
    val user: User,
    val channel: StateFlow<Channel>,
    val members: StateFlow<Map<User, Role>>
)

sealed interface ChannelInfoScreenState {
    data object Idle: ChannelInfoScreenState
    data object Loading: ChannelInfoScreenState
    data class Success(val channelInfo: ChannelInfo): ChannelInfoScreenState
    data class SuccessOnLeave(val channel: Channel): ChannelInfoScreenState
    data class Error(val error: ApiError, val onDismiss: () -> Unit): ChannelInfoScreenState
}

class ChannelInfoViewModel(
    private val userInfo: UserInfoRepository,
    private val repo: ChImpRepo,
    channel: Channel,
    initialState: ChannelInfoScreenState = ChannelInfoScreenState.Idle
) : ViewModel() {

    private val _state = MutableStateFlow<ChannelInfoScreenState>(initialState)
    val state = _state.asStateFlow()

    private val _channel = MutableStateFlow<Channel>(channel)
    val channelFlow = _channel.asStateFlow()

    private val _members = MutableStateFlow<Map<User, Role>>(emptyMap())
    val members = _members.asStateFlow()

    fun getChannelMembers(channel: Channel, onDismiss: () -> Unit = {}): Job? {
        if (_state.value == ChannelInfoScreenState.Loading) return null
        _state.value = ChannelInfoScreenState.Loading
        return viewModelScope.launch {
            try {
                val user = userInfo.getUserInfo() ?: throw Exception("User not authenticated")
                repo.channelRepo.fetchChannelMembers(channel).collect {
                    _members.value = it
                    _state.value = ChannelInfoScreenState.Success(ChannelInfo(user,
                        channelFlow, members))
                }
            } catch (e: Throwable) {
                ChannelInfoScreenState.Error(ApiError("Error fetching channel members"), onDismiss)
            }
        }
    }

    fun loadChannel(channel: Channel, onDismiss: () -> Unit = {}): Job? {
        return viewModelScope.launch {
            try {
                val user = userInfo.getUserInfo() ?: throw Exception("User not authenticated")
                repo.channelRepo.getChannel(channel).collect {
                    _channel.value = it
                    if (_members.value.isNotEmpty())
                    _state.value = ChannelInfoScreenState.Success(ChannelInfo(user,
                        channelFlow, members))
                }
            } catch (e: Throwable) {
                ChannelInfoScreenState.Error(ApiError("Error fetching channel"), onDismiss)
            }
        }
    }

    fun updateChannelName(
        channel: Channel,
        newName: String,
        onDismiss: () -> Unit
    ) {
        if (_state.value != ChannelInfoScreenState.Loading) {
            _state.value = ChannelInfoScreenState.Loading
            viewModelScope.launch {
                val user = userInfo.getUserInfo() ?: throw Exception("User not authenticated")
                _state.value = try {
                    when (val updatedChannel = repo.channelRepo.changeChannelName(channel, newName)) {
                        is Success -> ChannelInfoScreenState.Success(ChannelInfo(user,
                            channelFlow, members))
                        is Failure -> ChannelInfoScreenState.Error(updatedChannel.value, onDismiss)
                    }
                } catch (e: Throwable) {
                    ChannelInfoScreenState.Error(ApiError("Error updating channel name"), onDismiss)
                }
            }
        }
    }

    fun leaveChannel(channel: Channel, onDismiss: () -> Unit){
        if(_state.value != ChannelInfoScreenState.Loading) {
            _state.value = ChannelInfoScreenState.Loading
            viewModelScope.launch {
                _state.value = try {
                    val user = userInfo.getUserInfo() ?: throw Exception("User not authenticated")
                    when (val serviceChannel = repo.channelRepo.leaveChannel(channel, user)) {
                        is Success -> {
                            repo.messageRepo.deleteMessages(channel)
                            ChannelInfoScreenState.SuccessOnLeave(serviceChannel.value)
                        }
                        is Failure -> ChannelInfoScreenState.Error(serviceChannel.value, onDismiss)
                    }
                } catch (e: Throwable) {
                    ChannelInfoScreenState.Error(ApiError("Error leaving Channel"), onDismiss)
                }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class ChannelInfoViewModelFactory(
    private val userInfo: UserInfoRepository,
    private val repo: ChImpRepo,
    private val channel: Channel
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChannelInfoViewModel(
            userInfo,
            repo,
            channel
        ) as T
    }
}