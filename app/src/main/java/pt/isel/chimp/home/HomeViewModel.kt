package pt.isel.chimp.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.ChannelError
import pt.isel.chimp.service.ChannelService
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success


sealed interface HomeScreenState {
    data object Idle : HomeScreenState
    data object Loading : HomeScreenState
    data class Error(val exception: ChannelError) : HomeScreenState
    data class Success(val channels: List<Channel>) : HomeScreenState
}


class HomeScreenViewModel(private val channelService: ChannelService) : ViewModel() {

    var state: HomeScreenState by mutableStateOf<HomeScreenState>(HomeScreenState.Idle)
        private set

    fun fetchChannelsOfUser(user: User) {
        if (state != HomeScreenState.Loading) {
            state = HomeScreenState.Loading
            viewModelScope.launch {
                val channels = channelService.getChannelsByUser(user)
                state = when (channels) {
                    is Success -> HomeScreenState.Success(channels.value)
                    is Failure -> HomeScreenState.Error(channels.value)
                }
            }
        }
    }

}

@Suppress("UNCHECKED_CAST")
class HomeScreenViewModelFactory(private val service: ChImpService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>):  T {
        return HomeScreenViewModel(service.channelService) as T
    }
}