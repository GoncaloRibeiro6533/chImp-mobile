package pt.isel.chimp.channels.createInvitation

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
import pt.isel.chimp.service.http.utils.ChImpException
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success


sealed class CreateInvitationScreenState {
    data object Idle : CreateInvitationScreenState()
    data object Loading : CreateInvitationScreenState()
    data object Success : CreateInvitationScreenState()
    data object Typing : CreateInvitationScreenState()
    data class SearchUser(val query: String) : CreateInvitationScreenState()
    data class Error(val error: ApiError) : CreateInvitationScreenState()
    data class Submitting(val receiver: User, val role: String, val channel: Channel) : CreateInvitationScreenState()
}


class CreateInvitationViewModel(
    private val repo: UserInfoRepository,
    private val service: ChImpService,
    initialState : CreateInvitationScreenState = CreateInvitationScreenState.Idle
) : ViewModel() {

    private val _state = MutableStateFlow<CreateInvitationScreenState>(initialState)
    val state = _state.asStateFlow()

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users = _users.asStateFlow()


    fun searchUsers(query: String) {
        if (_state.value != CreateInvitationScreenState.Loading) {
            //    _state.value = CreateInvitationScreenState.Loading
            viewModelScope.launch {
                _state.value = try {
                    val user = repo.getUserInfo() ?: throw ChImpException("User not found", null)
                    when (val users = service.userService.findUserByUsername(query)) {
                        is Success -> {
                            _users.value = users.value.filter { it.id != user.id }
                            CreateInvitationScreenState.Idle
                        }
                        is Failure -> CreateInvitationScreenState.Error(users.value)
                    }
                } catch (e: Throwable) {
                    CreateInvitationScreenState.Error(ApiError("Error searching users"))
                }
            }
        }
    }

    fun inviteMember(channel: Channel, user: User?, role: Role?) {
        if (user == null || role == null) {
            _state.value = CreateInvitationScreenState.Error(ApiError("User and Role must be selected"))
            return
        }
        /*if (_state.value != CreateInvitationScreenState.Loading) {
            _state.value = CreateInvitationScreenState.Loading*/
        viewModelScope.launch {
            _state.value = try {
                when (val result = service.invitationService.createChannelInvitation(
                    user.id,
                    channel.id,
                    role
                )) {
                    is Success -> CreateInvitationScreenState.Success
                    is Failure -> CreateInvitationScreenState.Error(result.value)
                }
            } catch (e: Throwable) {
                CreateInvitationScreenState.Error(ApiError("Error inviting user to Channel"))
            }
        }
    }
    //}

    fun setIdle() {
        _state.value = CreateInvitationScreenState.Idle
    }
}




@Suppress("UNCHECKED_CAST")
class CreateInvitationViewModelFactory(
    private val repo: UserInfoRepository,
    private val service: ChImpService,
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CreateInvitationViewModel(
            repo,
            service
        ) as T
    }
}