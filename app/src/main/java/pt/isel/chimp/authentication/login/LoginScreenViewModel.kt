package pt.isel.chimp.authentication.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.chimp.domain.repository.UserInfoRepository
import pt.isel.chimp.domain.user.AuthenticatedUser
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.UserService
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success


sealed interface LoginScreenState {
    data object Idle : LoginScreenState
    data object Loading : LoginScreenState
    data class Success(val user: AuthenticatedUser) : LoginScreenState
    data class Error(val error: ApiError) : LoginScreenState
}

class LoginScreenViewModel(
    private val repo : UserInfoRepository,
    private val userService: UserService,
    initialState : LoginScreenState = LoginScreenState.Idle
) : ViewModel() {

        var state: LoginScreenState by mutableStateOf<LoginScreenState>(initialState)
            private set

    fun fetchLogin(username: String, password: String) {
            if (state != LoginScreenState.Loading) {
                state = LoginScreenState.Loading
                viewModelScope.launch {
                    state =
                    try {
                        val authUser = userService.login(username, password)
                        when (authUser) {
                            is Success -> {
                                //TODO update local database
                                repo.updateUserInfo(authUser.value)
                                LoginScreenState.Success(authUser.value)
                            }
                            is Failure -> LoginScreenState.Error(authUser.value)
                        }
                    } catch (e: Throwable) {
                        LoginScreenState.Error(ApiError("Error logging in"))
                    }
                }
            }
    }

    fun setIdleState() {
        state = LoginScreenState.Idle
    }

}

@Suppress("UNCHECKED_CAST")
class LoginScreenViewModelFactory(
    private val repo : UserInfoRepository,
    private val service: ChImpService
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>):  T {
        return LoginScreenViewModel(
            repo,
            service.userService
        ) as T
    }
}

