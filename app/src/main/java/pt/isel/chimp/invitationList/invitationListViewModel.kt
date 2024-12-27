package pt.isel.chimp.invitationList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.chimp.channels.channel.ChannelScreenState
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.invitation.Invitation
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.repository.ChImpRepo
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.InvitationService
import pt.isel.chimp.service.repo.InvitationRepoMock
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success

sealed interface InvitationListScreenState {
    data object Idle: InvitationListScreenState
    data object Loading: InvitationListScreenState
    data class Success(val invitations: List<Invitation>): InvitationListScreenState
    data class SuccessOnAccept(val channel: Channel): InvitationListScreenState
    data class  SuccessOnDecline(val check: Boolean): InvitationListScreenState
    data class Error(val error: ApiError): InvitationListScreenState

}

class InvitationListViewModel (
    private val invitationService: InvitationService,
    private val repo: ChImpRepo,
    initialState: InvitationListScreenState = InvitationListScreenState.Idle
): ViewModel() {

    private val _state = MutableStateFlow<InvitationListScreenState>(initialState)
    val state = _state.asStateFlow()

    fun getInvitations() {
        if (_state.value != InvitationListScreenState.Loading) {
            _state.value = InvitationListScreenState.Loading
            viewModelScope.launch {
                _state.value = try {
                    when(val invitations = invitationService.getInvitationsOfUser()) {
                        is Success -> InvitationListScreenState.Success(invitations.value)
                        is Failure -> InvitationListScreenState.Error(invitations.value)
                    }
                } catch (e: Throwable) {
                    InvitationListScreenState.Error(ApiError("Error fetching invitation list"))
                }
            }
        }
    }

    fun acceptInvitation(invitationId: Int) {
        if(_state.value != InvitationListScreenState.Loading) {
            _state.value = InvitationListScreenState.Loading
            viewModelScope.launch {
                _state.value = try {
                    when(val channel = invitationService.acceptInvitation(invitationId)) {
                        is Success -> InvitationListScreenState.SuccessOnAccept(channel.value)
                        is Failure -> InvitationListScreenState.Error(channel.value)
                    }
                } catch (e: Throwable) {
                    InvitationListScreenState.Error(ApiError(""))
                }
            }
        }
    }

    fun declineInvitation(invitationId: Int) {
        if(_state.value != InvitationListScreenState.Loading) {
            _state.value = InvitationListScreenState.Loading
            viewModelScope.launch {
                _state.value = try {
                    when(val check = invitationService.declineInvitation(invitationId)) {
                        is Success -> InvitationListScreenState.SuccessOnDecline(check.value)
                        is Failure -> InvitationListScreenState.Error(check.value)
                    }
                } catch (e: Throwable) {
                    InvitationListScreenState.Error(ApiError(""))
                }
            }
        }
    }

}

@Suppress("UNCHECKED_CAST")
class InvitationListViewModelFactory(
    private val service: ChImpService,
    private val repo: ChImpRepo
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InvitationListViewModel(
            service.invitationService,
            repo
        ) as T
    }
}