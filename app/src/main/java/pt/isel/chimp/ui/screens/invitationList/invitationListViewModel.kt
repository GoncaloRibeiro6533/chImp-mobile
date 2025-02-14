package pt.isel.chimp.ui.screens.invitationList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.invitation.Invitation
import pt.isel.chimp.domain.repository.UserInfoRepository
import pt.isel.chimp.domain.ApiError
import pt.isel.chimp.domain.Role
import pt.isel.chimp.service.http.utils.ChImpException
import pt.isel.chimp.repository.ChImpRepo
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success

sealed interface InvitationListScreenState {
    data object Uninitialized: InvitationListScreenState
    data object Loading: InvitationListScreenState
    data class Success(val invitations: StateFlow<List<Invitation>>): InvitationListScreenState
    data class SuccessOnAccept(val channelRole: Pair<Channel, Role>): InvitationListScreenState
    data class Error(val error: ApiError): InvitationListScreenState

}

class InvitationListViewModel (
    private val repo: ChImpRepo,
    private val userInfoRepo: UserInfoRepository,
    initialState: InvitationListScreenState = InvitationListScreenState.Uninitialized
): ViewModel() {

    private val _state = MutableStateFlow<InvitationListScreenState>(initialState)
    val state = _state.asStateFlow()

    private val _invitations = MutableStateFlow<List<Invitation>>(emptyList())
    val invitations = _invitations.asStateFlow()


    fun loadLocalData(): Job? {
        if (_state.value == InvitationListScreenState.Loading) return null
        _state.value = InvitationListScreenState.Loading
        return viewModelScope.launch {
            try {
                val userInfo = userInfoRepo.getUserInfo() ?: throw ChImpException("User not found", null)
                repo.invitationRepo.fetchInvitations(userInfo).collect{
                    _invitations.value = it
                    _state.value = InvitationListScreenState.Success(invitations)
                }
            } catch (e: Throwable) {
                _state.value = InvitationListScreenState.Error(ApiError(e.message ?: "Error fetching data"))
            }

        }
    }

    fun acceptInvitation(invitation: Invitation) {
        if(_state.value != InvitationListScreenState.Loading) {
            _state.value = InvitationListScreenState.Loading
            viewModelScope.launch {
                _state.value = try {
                    val user = userInfoRepo.getUserInfo() ?: throw ChImpException("User not found", null)
                    when(val channel = repo.invitationRepo.acceptInvitation(invitation)) {
                        is Success -> {
                            repo.channelRepo.insertChannels(
                                user.id, mapOf(channel.value to invitation.role))
                            InvitationListScreenState.SuccessOnAccept(Pair(channel.value, invitation.role))
                        }
                        is Failure -> {
                            InvitationListScreenState.Error(channel.value)
                        }
                    }
                } catch (e: Throwable) {
                    InvitationListScreenState.Error(ApiError(e.message ?: "Error accepting invitation"))
                }
            }
        }
    }

    fun declineInvitation(invitation: Invitation) {
        if(_state.value != InvitationListScreenState.Loading) {
            _state.value = InvitationListScreenState.Loading
            viewModelScope.launch {
                _state.value = try {
                    when(val result = repo.invitationRepo.declineInvitation(invitation)) {
                        is Success -> InvitationListScreenState.Success(invitations)
                        is Failure -> InvitationListScreenState.Error(result.value)
                    }
                } catch (e: Throwable) {
                    InvitationListScreenState.Error(ApiError(e.message ?: "Error declining invitation"))
                }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class InvitationListViewModelFactory(
    private val repo: ChImpRepo,
    private val userInfoRepo: UserInfoRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InvitationListViewModel(
            repo,
            userInfoRepo
        ) as T
    }
}