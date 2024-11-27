package pt.isel.chimp.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.chimp.domain.profile.Profile
import pt.isel.chimp.domain.repository.UserInfoRepository
import pt.isel.chimp.domain.user.AuthenticatedUser
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
    private val userServices: UserService) : ViewModel() {

    var state: ProfileScreenState by mutableStateOf<ProfileScreenState>(ProfileScreenState.Idle)
        private set

    fun fetchProfile() {
        if (state != ProfileScreenState.Loading) {
                state = ProfileScreenState.Loading
            viewModelScope.launch {
                state = try {
                    val userInfo = repo.getUserInfo() ?: throw Exception("User not authenticated")
                    val user = userServices.findUserById(userInfo.token, userInfo.user.id)
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
        if (state != ProfileScreenState.Loading) {
            state = ProfileScreenState.Loading
            viewModelScope.launch {
                state = try {
                    val userInfo = repo.getUserInfo() ?: throw Exception("User not authenticated")
                    val user = userServices.updateUsername(newUsername, userInfo.token)
                    when (user) {
                        is Success -> {
                            repo.updateUserInfo(AuthenticatedUser(user.value, userInfo.token))
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
        if (state != ProfileScreenState.EditingUsername(profile)) {
            state = ProfileScreenState.EditingUsername(profile)
        }
    }

    fun setSuccessState(profile: Profile) {
        if (state != ProfileScreenState.Success(profile)) {
            state = ProfileScreenState.Success(profile)
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


