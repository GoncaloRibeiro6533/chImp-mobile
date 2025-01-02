package pt.isel.chimp.channels.channelInfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.repository.UserInfoRepository
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.domain.ApiError
import pt.isel.chimp.repository.ChImpRepo
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success

data class ChannelInfo(
    val user: User,
    val channel: Channel,
    val members: List<Pair<User, Role>>
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
    private val service: ChImpService,
    private val repo: ChImpRepo,
    initialState: ChannelInfoScreenState = ChannelInfoScreenState.Idle
) : ViewModel() {

    private val _state = MutableStateFlow<ChannelInfoScreenState>(initialState)
    val state = _state.asStateFlow()

    private val _channel = MutableStateFlow<Channel?>(null)
    val channel = _channel.asStateFlow()

    private val _members = MutableStateFlow<Map<User, Role>>(emptyMap())
    val members = _members.asStateFlow()

    fun getChannelMembers(channel: Channel, onDismiss: () -> Unit = {}) {
        if (_state.value != ChannelInfoScreenState.Loading) {
            _state.value = ChannelInfoScreenState.Loading
            viewModelScope.launch {
                _state.value = try {
                    val user = userInfo.getUserInfo() ?: throw Exception("User not authenticated")
                    when (val members = service.channelService.getChannelMembers(channel.id)) {
                        is Success -> ChannelInfoScreenState.Success(ChannelInfo(user, channel, members.value))
                        is Failure -> ChannelInfoScreenState.Error(members.value, onDismiss)
                    }
                } catch (e: Throwable) {
                    ChannelInfoScreenState.Error(ApiError("Error fetching channel members"), onDismiss)
                }
            }
        }
    }

    fun updateChannelName(
        channel: ChannelInfo,
        newName: String,
        onDismiss: () -> Unit
        ) {
        if (_state.value != ChannelInfoScreenState.Loading) {
            _state.value = ChannelInfoScreenState.Loading
            viewModelScope.launch {
                val user = userInfo.getUserInfo() ?: throw Exception("User not authenticated")
                _state.value = try {
                    when (val updatedChannel = service.channelService.updateChannelName(channel.channel.id,newName)) {
                        is Success -> ChannelInfoScreenState.Success(ChannelInfo(user,updatedChannel.value, channel.members))
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
                    when (val serviceChannel = service.channelService.removeUserFromChannel(channel.id, user.id)) {
                        is Success -> {
                            repo.messageRepo.deleteMessages(channel)
                            repo.channelRepo.removeUserFromChannel(user.id, channel.id)
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
    private val service: ChImpService,
    private val repo: ChImpRepo
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChannelInfoViewModel(
            userInfo,
            service,
            repo
        ) as T
    }
}