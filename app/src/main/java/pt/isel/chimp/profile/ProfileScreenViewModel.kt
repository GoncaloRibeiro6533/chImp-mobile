package pt.isel.chimp.profile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.chimp.domain.profile.Profile
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.UserError
import pt.isel.chimp.service.UserService
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success


sealed interface ProfileScreenState {

    data object Idle : ProfileScreenState
    data object Loading : ProfileScreenState
    data class Success(val profile: Profile) : ProfileScreenState
    data class EditingUsername(val profile: Profile) : ProfileScreenState
    data class Error(val exception: UserError) : ProfileScreenState

}

class ProfileScreenViewModel(private val userServices: UserService) : ViewModel() {

    var state: ProfileScreenState by mutableStateOf<ProfileScreenState>(ProfileScreenState.Idle)
        private set

    fun fetchProfile(token: String) {
        if (state != ProfileScreenState.Loading) {
                state = ProfileScreenState.Loading
            viewModelScope.launch {
                val user = userServices.fetchUser(token)
                state = when (user) {
                    is Success -> ProfileScreenState
                        .Success(Profile(user.value.username, user.value.email))
                    is Failure -> ProfileScreenState.Error(user.value)
                }
                /*
                state = try {
                    val user = userServices.fetchUser(token)
                    val state = state is ProfileScreenState.Loading
                    Log.d("StateOfProfile", state.toString())
                    ProfileScreenState.Success(Profile(user.username, user.email))
                } catch (e: Throwable) {
                    ProfileScreenState.Error(e)
                }
                 */
            }
        }
    }

    fun editUsername(newUsername: String, token: String) {
        if (state != ProfileScreenState.Loading) {
            state = ProfileScreenState.Loading
            viewModelScope.launch {
                val user = userServices.updateUsername(newUsername, token)
                state = when (user) {
                    is Success -> ProfileScreenState
                        .Success(Profile(user.value.username, user.value.email))
                    is Failure -> ProfileScreenState.Error(user.value)
                }
                /**
                state = try {
                    val user = userServices.updateUsername(newUsername, token)
                    ProfileScreenState.Success(Profile(user.username, user.email))
                } catch (e: Throwable) {
                    ProfileScreenState.Error(e)
                }
                */
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
class ProfileScreenViewModelFactory(private val service: ChImpService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>):  T {
        return ProfileScreenViewModel(service.userService) as T
    }
}


