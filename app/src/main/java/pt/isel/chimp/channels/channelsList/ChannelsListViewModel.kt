package pt.isel.chimp.channels.channelsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.repository.UserInfoRepository
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.http.utils.ChImpException
import pt.isel.chimp.repository.ChImpRepo
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.ChannelService
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success

sealed interface ChannelsListScreenState {
    data object Initialized : ChannelsListScreenState
    data object Idle : ChannelsListScreenState
    data object Loading : ChannelsListScreenState
    data class Success(val channels: Map<Channel,Role>) : ChannelsListScreenState
    data class Error(val error: ApiError) : ChannelsListScreenState
}

class ChannelsListViewModel(
    private val userInfo: UserInfoRepository,
    private val channelService: ChannelService,
    private val repo: ChImpRepo,
    initialState: ChannelsListScreenState = ChannelsListScreenState.Idle
) : ViewModel() {

    private val _state = MutableStateFlow<ChannelsListScreenState>(initialState)
    val state = _state.asStateFlow()
    private val _channels = MutableSharedFlow<Map<Channel,Role>>(replay = 1)
    val channels: SharedFlow<Map<Channel,Role>> = _channels.asSharedFlow()

    fun getChannels() {
        //TODO if is not initialized get from local storage else fetch from server
        if (_state.value != ChannelsListScreenState.Loading) {
            _state.value = ChannelsListScreenState.Loading
            viewModelScope.launch {
                repo.channelRepo.getChannels().collect { channels ->
              //  repo.channelRepo.getChannels()
                    if (channels.isNotEmpty()) {
                        _state.value = ChannelsListScreenState.Success(channels)
                        return@collect
                    }
                    _state.value = try {
                        val user = userInfo.getUserInfo() ?: throw ChImpException(message = "User not found", null)
                        when (val channels = channelService.getChannelsOfUser(user.id)) {
                            is Success -> {
                                repo.channelRepo.insertChannels(user.id, channels.value)
                                ChannelsListScreenState.Success(channels.value)
                            }
                            is Failure -> ChannelsListScreenState.Error(channels.value)
                        }
                    } catch (e: Throwable) {
                        ChannelsListScreenState.Error(ApiError("Error getting channels"))
                    }
            }
        }
    }
}
}

    @Suppress("UNCHECKED_CAST")
    class ChannelsListViewModelFactory(
        private val userInfo: UserInfoRepository,
        private val service: ChImpService,
        //private val db: ChImpClientDB
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