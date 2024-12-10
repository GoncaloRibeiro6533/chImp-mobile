package pt.isel.chimp.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pt.isel.chimp.domain.profile.Profile
import pt.isel.chimp.domain.repository.UserInfoRepository
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.UserService
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success


sealed interface ProfileScreenState {

    data object Idle : ProfileScreenState
    data object Loading : ProfileScreenState
    data class Success(val profile: Profile) : ProfileScreenState
    data class EditingUsername(val profile: Profile) : ProfileScreenState
    data class Error(val error: ApiError) : ProfileScreenState

}

class ProfileScreenViewModel(
    private val repo: UserInfoRepository,
    private val userServices: UserService,
    initialState: ProfileScreenState = ProfileScreenState.Idle
    ) : ViewModel() {

    private val _screenState = MutableStateFlow<ProfileScreenState>(initialState)
    val state: StateFlow<ProfileScreenState> = _screenState


    fun fetchProfile() {
        if (_screenState.value != ProfileScreenState.Loading) {
                _screenState.value = ProfileScreenState.Loading
            viewModelScope.launch {
                _screenState.value = try {
                    val userInfo = repo.getUserInfo() ?: throw Exception("User not authenticated")
                    val user = userServices.findUserById(userInfo.id)
                    when (user) {
                        is Success ->
                            ProfileScreenState.Success(Profile(user.value.username, user.value.email))
                        is Failure -> ProfileScreenState.Error(user.value)
                    }
                } catch (e: Throwable) {
                    ProfileScreenState.Error(ApiError("Error fetching user"))
                }
            }
        }
    }

    fun editUsername(newUsername: String) {
        if (_screenState.value != ProfileScreenState.Loading) {
            _screenState.value = ProfileScreenState.Loading
            viewModelScope.launch {
                _screenState.value = try {
                    val userInfo = repo.getUserInfo() ?: throw Exception("User not authenticated")
                    val user = userServices.updateUsername(newUsername)
                    when (user) {
                        is Success -> {
                            repo.updateUserInfo(user.value)
                            //TODO: update local storage
                            ProfileScreenState.Success(Profile(user.value.username, user.value.email))
                        }
                        is Failure -> ProfileScreenState.Error(user.value)
                    }
                } catch (e: Throwable) {
                    ProfileScreenState.Error(ApiError("Error updating username"))
                }
            }
        }
    }

    fun setEditState(profile: Profile) {
        if (_screenState.value != ProfileScreenState.EditingUsername(profile)) {
            _screenState.value = ProfileScreenState.EditingUsername(profile)
        }
    }

    fun setSuccessState(profile: Profile) {
        if (_screenState.value != ProfileScreenState.Success(profile)) {
            _screenState.value = ProfileScreenState.Success(profile)
        }
    }

}

@Suppress("UNCHECKED_CAST")
class ProfileScreenViewModelFactory(
    private val repo: UserInfoRepository,
    private val service: ChImpService
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>):  T {
        return ProfileScreenViewModel(
            repo,
            service.userService
        ) as T
    }
}


