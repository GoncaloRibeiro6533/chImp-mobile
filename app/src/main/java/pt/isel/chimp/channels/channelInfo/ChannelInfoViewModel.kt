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
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.ChannelService
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success

data class ChannelInfo(
    val channel: Channel,
    val members: List<Pair<User, Role>>
)

sealed interface ChannelInfoScreenState {
    data object Idle: ChannelInfoScreenState
    data object Loading: ChannelInfoScreenState
    data class Success(val channelInfo: ChannelInfo): ChannelInfoScreenState
    data class SuccessOnLeave(val channel: Channel): ChannelInfoScreenState
    data class Error(val error: ApiError): ChannelInfoScreenState
}

class ChannelInfoViewModel(
    private val repo: UserInfoRepository,
    private val channelService: ChannelService,
    initialState: ChannelInfoScreenState = ChannelInfoScreenState.Idle
) : ViewModel() {

    private val _state = MutableStateFlow<ChannelInfoScreenState>(initialState)
    val state = _state.asStateFlow()

    fun getChannelMembers(channel: Channel) {
        if (_state.value != ChannelInfoScreenState.Loading) {
            _state.value = ChannelInfoScreenState.Loading
            viewModelScope.launch {
                _state.value = try {
                    when (val members = channelService.getChannelMembers(channel.id)) {
                        is Success -> ChannelInfoScreenState.Success(ChannelInfo(channel, members.value))
                        is Failure -> ChannelInfoScreenState.Error(members.value)
                    }
                } catch (e: Throwable) {
                    ChannelInfoScreenState.Error(ApiError("Error fetching channel members"))
                }
            }
        }
    }

    fun updateChannelName(
        channel: ChannelInfo,
        newName: String) {
        if (_state.value != ChannelInfoScreenState.Loading) {
            _state.value = ChannelInfoScreenState.Loading
            viewModelScope.launch {
                _state.value = try {
                    when (val updatedChannel = channelService.updateChannelName(channel.channel.id,newName)) {
                        is Success -> ChannelInfoScreenState.Success(ChannelInfo(updatedChannel.value, channel.members))
                        is Failure -> ChannelInfoScreenState.Error(updatedChannel.value)
                    }
                } catch (e: Throwable) {
                    ChannelInfoScreenState.Error(ApiError("Error updating channel name"))
                }
            }
        }
    }

    fun leaveChannel(channel: Channel){
        if(_state.value != ChannelInfoScreenState.Loading) {
            _state.value = ChannelInfoScreenState.Loading
            viewModelScope.launch {
                _state.value = try {
                    val user = repo.getUserInfo() ?: throw Exception("User not authenticated")
                    when (val serviceChannel = channelService.removeUserFromChannel(channel.id, user.id)) {
                        is Success -> ChannelInfoScreenState.SuccessOnLeave(serviceChannel.value)
                        is Failure -> ChannelInfoScreenState.Error(serviceChannel.value)
                    }
                } catch (e: Throwable) {
                    ChannelInfoScreenState.Error(ApiError("Error leaving Channel"))
                }
            }
        }
    }

}

@Suppress("UNCHECKED_CAST")
class ChannelInfoViewModelFactory(
    private val repo: UserInfoRepository,
    private val service: ChImpService
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChannelInfoViewModel(
            repo,
            service.channelService
        ) as T
    }
}