package pt.isel.chimp.channels.channelInfo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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

    var state: ChannelInfoScreenState by mutableStateOf(initialState)
        private set


    fun getChannelMembers(channel: Channel) {
        if (state != ChannelInfoScreenState.Loading) {
            state = ChannelInfoScreenState.Loading
            viewModelScope.launch {
                state = try {
                    val token = repo.getUserInfo()?.token ?: throw Exception("User not authenticated")
                    when (val members = channelService.getChannelMembers(token, channel.id)) {
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
        if (state != ChannelInfoScreenState.Loading) {
            state = ChannelInfoScreenState.Loading
            viewModelScope.launch {
                state = try {
                    val user = repo.getUserInfo() ?: throw Exception("User not authenticated")
                    when (val updatedChannel = channelService.updateChannelName(user.token, channel.channel.id,newName)) {
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
        if(state != ChannelInfoScreenState.Loading) {
            state = ChannelInfoScreenState.Loading
            viewModelScope.launch {
                state = try {
                    val user = repo.getUserInfo() ?: throw Exception("User not authenticated")
                    when (val serviceChannel = channelService.removeUserFromChannel(user.token, channel.id, user.user.id)) {
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