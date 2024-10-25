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


sealed interface ProfileScreenState {


    data object Idle : ProfileScreenState
    data object Loading : ProfileScreenState
    data class Success(val profile: Profile) : ProfileScreenState
    data class EditingUsername(val profile: Profile) : ProfileScreenState
    data class Error(val exception: Throwable) : ProfileScreenState

}

class ProfileScreenViewModel(private val services: ChImpService) : ViewModel() {

    var state: ProfileScreenState by mutableStateOf<ProfileScreenState>(ProfileScreenState.Idle)
        private set

    fun fetchProfile(token: String) {
        if (state != ProfileScreenState.Loading) {
            viewModelScope.launch {
                state = ProfileScreenState.Loading
                state = try {
                    val user = services.userService.fetchUser(token)
                    val state = state is ProfileScreenState.Loading
                    Log.d("StateOfProfile", state.toString())
                    ProfileScreenState.Success(Profile(user.username, user.email))
                } catch (e: Throwable) {
                    ProfileScreenState.Error(e)
                }
            }
        }
    }

    fun editUsername(newUsername: String, token: String) {
        if (state != ProfileScreenState.Loading) {
            state = ProfileScreenState.Loading
            viewModelScope.launch {
                state = try {
                    val user = services.userService.updateUsername(newUsername, token)
                    ProfileScreenState.Success(Profile(user.username, user.email))
                } catch (e: Throwable) {
                    ProfileScreenState.Error(e)
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
class ProfileScreenViewModelFactory(private val service: ChImpService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>):  T {
        return ProfileScreenViewModel(service) as T
    }
}


