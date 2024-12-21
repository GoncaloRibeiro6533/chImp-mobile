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
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.http.utils.ChImpException
import pt.isel.chimp.repository.ChImpRepo
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.ChannelService
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success

sealed interface ChannelsListScreenState {
    data object Uninitialized : ChannelsListScreenState
    data object LoadingUserInfo: ChannelsListScreenState
    data class LoadFromRemote(val userInfo: User) : ChannelsListScreenState
    data class SaveData(val user: User, val channels: Map<Channel,Role>) : ChannelsListScreenState
    data object Loading : ChannelsListScreenState
    data class Success(val channels: StateFlow<Map<Channel,Role>>) : ChannelsListScreenState
    data class Error(val error: ApiError) : ChannelsListScreenState
}
/*
class ChannelsListViewModel(
    private val userInfo: UserInfoRepository,
    private val channelService: ChannelService,
    private val repo: ChImpRepo,
    initialState: ChannelsListScreenState = ChannelsListScreenState.Uninitialized
) : ViewModel() {

    private val _state = MutableStateFlow<ChannelsListScreenState>(initialState)
    val state = _state.asStateFlow()
    //  private val _channels = MutableSharedFlow<Map<Channel,Role>>()
//    val channels: SharedFlow<Map<Channel,Role>> = _channels.asSharedFlow()
    private val _channels = MutableStateFlow<Map<Channel,Role>>(emptyMap())
    val channels: StateFlow<Map<Channel,Role>> = _channels.asStateFlow()

    fun loadUserInfoData() : Job? {
        if(_state.value !is ChannelsListScreenState.Uninitialized) return null
        return viewModelScope.launch {
            try {
                val user = userInfo.getUserInfo() ?: throw ChImpException(message = "User not found", null)
                _state.value = ChannelsListScreenState.Initialized(user)
            } catch (e: Exception) {
                _state.value = ChannelsListScreenState.Error(ApiError("Error getting user info"))
            }
        }
    }

    fun loadData() : Job? {
        if(_state.value !is ChannelsListScreenState.Initialized) return null

        _state.value = ChannelsListScreenState.Loading
        return viewModelScope.launch {
            repo.channelRepo.getChannels().collect{ stream ->
                if(stream.isNotEmpty()) {
                    _channels.value = stream
                    _state.value = ChannelsListScreenState.Success(channels)
                    return@collect
                }
                _state.value =
                    try {
                        when(val result = channelService.getChannelsOfUser(userId = _){ {
                            is Success -> {
                                repo.channelRepo.insertChannels(user.id, result.value)
                                _channels.value = stream
                                ChannelsListScreenState.Success(channels)
                            }
                            is Failure ->ChannelsListScreenState.Error(result.value)
                        }
                    } catch (e: Throwable) {
                        ChannelsListScreenState.Error(ApiError("Error getting channels"))
                    }
            }
        }
    }

    fun getChannels(){

    }
    /*fun getChannels() {
        if (_state.value != ChannelsListScreenState.Loading) {
            _state.value = ChannelsListScreenState.Loading
            viewModelScope.launch {
                repo.channelRepo.getChannels().collect { stream ->
                    if (stream.isNotEmpty()) {
                        Log.d("ChannelsListViewModel", "ChannelsListViewModel: getChannels: stream is not empty")
                        _channels.emit(stream)
                        _state.value = ChannelsListScreenState.Success(channels)
                        return@collect
                    }
                    _state.value = try {
                        Log.d("ChannelsListViewModel", "ChannelsListViewModel: gettingChannels")
                        val user =   userInfo.getUserInfo() ?: throw ChImpException(message = "User not found", null)
                        when (val result = channelService.getChannelsOfUser(user.id)) {
                            is Success -> {
                                repo.channelRepo.insertChannels(user.id, result.value)
                                _channels.emit(stream)
                                if (stream.isNotEmpty()) ChannelsListScreenState.Success(channels)
                                else ChannelsListScreenState.Loading
                            }
                            is Failure -> ChannelsListScreenState.Error(result.value)
                        }
                    } catch (e: Throwable) {
                        ChannelsListScreenState.Error(ApiError("Error getting channels"))
                    }
                }
            }
        }
    }*/

}
*/
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


    fun loadUserInfoData(){
        if (_channels.value.isNotEmpty()) return //TODO
        if (_state.value is ChannelsListScreenState.LoadingUserInfo) {
            viewModelScope.launch {
                try {

                    val user =
                        userInfo.getUserInfo() ?: throw ChImpException(message = "User not found", null)
                    _state.value = ChannelsListScreenState.LoadFromRemote(user)

                } catch (e: Exception) {
                    _state.value = ChannelsListScreenState.Error(ApiError("Error getting user info"))
                }
            }
        }
    }

    fun loadLocalData(): Job? {
        if (_state.value !is ChannelsListScreenState.Uninitialized) return null
        return viewModelScope.launch {
            try {
                repo.channelRepo.getChannels().collect { stream ->
                    _channels.value = stream
                    if (stream.isNotEmpty()) {
                        _state.value = ChannelsListScreenState.Success(channels)
                    }
                    if (stream.isEmpty() && _state.value is ChannelsListScreenState.Uninitialized) {
                        _state.value = ChannelsListScreenState.LoadingUserInfo
                    }
                }
            } catch (e: Throwable) {
                _state.value = ChannelsListScreenState.Error(ApiError("Error getting channels: ${e.message}"))
            }
        }
    }


    fun loadRemoteData(user: User) {
        if (_state.value is ChannelsListScreenState.LoadFromRemote) {
            viewModelScope.launch {
                try {
                    when (val result = channelService.getChannelsOfUser(user.id)) {
                        is Success -> _state.value = ChannelsListScreenState.SaveData(user, result.value)
                        is Failure -> {
                            _state.value = ChannelsListScreenState.Error(result.value)
                        }
                    }
                }catch (e: Throwable) {
                    _state.value =
                        ChannelsListScreenState.Error(ApiError("Error getting channels: ${e.message}"))
                }
            }
        }
    }

    fun saveData(user: User, channels: Map<Channel, Role>) {
        if (_state.value is ChannelsListScreenState.SaveData) {
            viewModelScope.launch {
                try {
                    repo.channelRepo.insertChannels(user.id, channels)
                } catch (e: Throwable) {
                    _state.value = ChannelsListScreenState.Error(ApiError("Error saving channels: ${e.message}"))
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