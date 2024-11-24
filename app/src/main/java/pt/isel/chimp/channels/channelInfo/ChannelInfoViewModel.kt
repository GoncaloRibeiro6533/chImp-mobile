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
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.ChannelService
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success

sealed interface ChannelInfoScreenState {
    data object Idle: ChannelInfoScreenState
    data object Loading: ChannelInfoScreenState
    data class Success(val channelMembers: List<Pair<User, Role>>): ChannelInfoScreenState
    data class SuccessOnLeave(val channel: Channel): ChannelInfoScreenState
    data class Error(val error: ApiError): ChannelInfoScreenState
}

class ChannelInfoViewModel(private val channelService: ChannelService) : ViewModel() {

    var state: ChannelInfoScreenState by mutableStateOf(ChannelInfoScreenState.Idle)
        private set


    fun getChannelMembers(token: String, channelId: Int) {
        if (state != ChannelInfoScreenState.Loading) {
            state = ChannelInfoScreenState.Loading
            viewModelScope.launch {
                state = try {
                    when (val members = channelService.getChannelMembers(token, channelId)) {
                        is Success -> ChannelInfoScreenState.Success(members.value)
                        is Failure -> ChannelInfoScreenState.Error(members.value)
                    }
                } catch (e: Throwable) {
                    ChannelInfoScreenState.Error(ApiError("Error fetching channel members"))
                }
            }
        }
    }

    fun updateChannelName() {
        TODO()
    }

    fun leaveChannel(token: String, channel: Channel, user: User){
        if(state != ChannelInfoScreenState.Loading) {
            state = ChannelInfoScreenState.Loading
            viewModelScope.launch {
                state = try {
                    when (val serviceChannel = channelService.removeUserFromChannel(token, channel.id, user.id)) {
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
class ChannelInfoViewModelFactory(private val service: ChImpService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChannelInfoViewModel(service.channelService) as T
    }
}