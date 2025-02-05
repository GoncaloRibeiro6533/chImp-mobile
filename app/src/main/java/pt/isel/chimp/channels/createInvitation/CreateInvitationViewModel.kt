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
import pt.isel.chimp.repository.ChImpRepo
import pt.isel.chimp.service.http.utils.ChImpException
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
    private val userInfoRepository: UserInfoRepository,
    private val repo: ChImpRepo,
    initialState : CreateInvitationScreenState = CreateInvitationScreenState.Idle
) : ViewModel() {

    private val _state = MutableStateFlow<CreateInvitationScreenState>(initialState)
    val state = _state.asStateFlow()

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users = _users.asStateFlow()


    fun searchUsers(query: String) {
        if (_state.value != CreateInvitationScreenState.SearchUser(query)) {
            _state.value = CreateInvitationScreenState.SearchUser(query)
            viewModelScope.launch {
                _state.value = try {
                    val user = userInfoRepository.getUserInfo() ?: throw ChImpException("User not found", null)
                    when (val users = repo.userRepo.fetchByUsername(query)) {
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

    fun inviteMember(channel: Channel, user: User, role: Role) {
        if (_state.value != CreateInvitationScreenState.Loading) {
            _state.value = CreateInvitationScreenState.Loading
            viewModelScope.launch {
                _state.value = try {
                    when (val result = repo.invitationRepo.createInvitation(
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
    }

    fun setIdle() {
        _state.value = CreateInvitationScreenState.Idle
    }
}




@Suppress("UNCHECKED_CAST")
class CreateInvitationViewModelFactory(
    private val userInfoRepository: UserInfoRepository,
    private val repo: ChImpRepo,
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CreateInvitationViewModel(
            userInfoRepository,
            repo
        ) as T
    }
}