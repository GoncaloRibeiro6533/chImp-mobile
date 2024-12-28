package pt.isel.chimp.invitationList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.invitation.Invitation
import pt.isel.chimp.domain.repository.UserInfoRepository
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.http.utils.ChImpException
import pt.isel.chimp.repository.ChImpRepo
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.InvitationService
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success

sealed interface InvitationListScreenState {
    data object Uninitialized: InvitationListScreenState
    data object Idle: InvitationListScreenState
    data object Loading: InvitationListScreenState
    data class Success(val invitations: StateFlow<List<Invitation>>): InvitationListScreenState
    data class SavingData(val invitations: List<Invitation>): InvitationListScreenState
    data class SuccessOnAccept(val channel: Channel): InvitationListScreenState
    data class  SuccessOnDecline(val check: Boolean): InvitationListScreenState
    data class Error(val error: ApiError): InvitationListScreenState

}

class InvitationListViewModel (
    private val invitationService: InvitationService,
    private val repo: ChImpRepo,
    private val userInfoRepo: UserInfoRepository,
    initialState: InvitationListScreenState = InvitationListScreenState.Uninitialized
): ViewModel() {

    private val _state = MutableStateFlow<InvitationListScreenState>(initialState)
    val state = _state.asStateFlow()

    private val _invitations = MutableStateFlow<List<Invitation>>(emptyList())
    val invitations = _invitations.asStateFlow()


    fun loadLocalData() {
        viewModelScope.launch {
            try {
                val userInfo = userInfoRepo.getUserInfo() ?: throw ChImpException("User not found", null)
                repo.invitationRepo.getInvitations(userInfo).collect{
                    _invitations.value = it
                    if (it.isNotEmpty() && _state.value == InvitationListScreenState.Uninitialized) {
                        _state.value = InvitationListScreenState.Success(invitations)
                    }
                    if (it.isEmpty() && _state.value == InvitationListScreenState.Uninitialized) {
                        _state.value = InvitationListScreenState.Idle
                    }
                }
            } catch (e: Throwable) {
                _state.value = InvitationListScreenState.Error(ApiError("Error loading invitations"))
            }

        }
    }

    fun getInvitations() {
        if (_state.value != InvitationListScreenState.Loading) {
            _state.value = InvitationListScreenState.Loading
            viewModelScope.launch {
                _state.value = try {
                    when(val invitations = invitationService.getInvitationsOfUser()) {
                        is Success -> {
                            repo.invitationRepo.insertInvitations(invitations.value)
                            InvitationListScreenState.SavingData(invitations.value)
                        }
                        is Failure -> InvitationListScreenState.Error(invitations.value)
                    }
                } catch (e: Throwable) {
                    InvitationListScreenState.Error(ApiError("Error fetching invitation list"))
                }
            }
        }
    }

    fun saveInvitations(invitationsList: List<Invitation>) {
        if (_state.value is InvitationListScreenState.SavingData) {
            viewModelScope.launch {
                _state.value = try {
                    repo.invitationRepo.insertInvitations(invitationsList)
                    InvitationListScreenState.Success(invitations)
                } catch (e: Throwable) {
                    InvitationListScreenState.Error(ApiError("Error saving invitations"))
                }
            }
        }
    }

    fun acceptInvitation(invitation: Invitation) {
        if(_state.value != InvitationListScreenState.Loading) {
            _state.value = InvitationListScreenState.Loading
            viewModelScope.launch {
                _state.value = try {
                    val user = userInfoRepo.getUserInfo() ?: throw ChImpException("User not found", null)
                    when(val channel = invitationService.acceptInvitation(invitation.id)) {
                        is Success -> {
                            repo.channelRepo.insertChannels(
                                user.id, mapOf(channel.value to invitation.role))
                            InvitationListScreenState.SuccessOnAccept(channel.value)
                        }
                        is Failure -> {
                            if (channel.value == ApiError("Invitation expired")) {
                                repo.invitationRepo.deleteInvitation(invitation.id)
                            }
                            InvitationListScreenState.Error(channel.value)
                        }
                    }
                } catch (e: Throwable) {
                    InvitationListScreenState.Error(ApiError(""))
                }
            }
        }
    }

    fun declineInvitation(invitation: Invitation) {
        if(_state.value != InvitationListScreenState.Loading) {
            _state.value = InvitationListScreenState.Loading
            viewModelScope.launch {
                _state.value = try {
                    when(val check = invitationService.declineInvitation(invitation.id)) {
                        is Success -> {
                            repo.invitationRepo.deleteInvitation(invitation.id)
                            InvitationListScreenState.Success(invitations)
                        }
                        is Failure -> {
                            if (check.value == ApiError("Invitation expired")) {
                                repo.invitationRepo.deleteInvitation(invitation.id)
                            }
                            InvitationListScreenState.Error(check.value)
                        }
                    }
                } catch (e: Throwable) {
                    InvitationListScreenState.Error(ApiError(""))
                }
            }
        }
    }

    fun setUninitialized() {
        _state.value = InvitationListScreenState.Success(invitations)
    }

}

@Suppress("UNCHECKED_CAST")
class InvitationListViewModelFactory(
    private val service: ChImpService,
    private val repo: ChImpRepo,
    private val userInfoRepo: UserInfoRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InvitationListViewModel(
            service.invitationService,
            repo,
            userInfoRepo
        ) as T
    }
}