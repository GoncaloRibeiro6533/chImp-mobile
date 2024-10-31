package pt.isel.chimp.authentication.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.chimp.domain.user.AuthenticatedUser
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.UserError
import pt.isel.chimp.service.UserService
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success


sealed interface LoginScreenState {
    data object Idle : LoginScreenState
    data object Loading : LoginScreenState
    data class Success(val user: AuthenticatedUser) : LoginScreenState
    data class Error(val exception: UserError) : LoginScreenState
}

class LoginScreenViewModel(private val userService: UserService ) : ViewModel() {

        var state: LoginScreenState by mutableStateOf<LoginScreenState>(LoginScreenState.Idle)
            private set

    fun fetchLogin(username: String, password: String) {
            if (state != LoginScreenState.Loading) {
                state = LoginScreenState.Loading
                viewModelScope.launch {
                    val authUser = userService.login(username, password)
                    state = when (authUser) {
                        is Success -> LoginScreenState.Success(authUser.value)
                        is Failure -> LoginScreenState.Error(authUser.value)
                    }
                    /**
                    state = try {
                        val user = userService.login(username, password)
                        LoginScreenState.Success(user)
                    } catch (e: Throwable) {
                        LoginScreenState.Error(e)
                    }

                     */
                }
            }
    }

    fun setIdleState() {
        state = LoginScreenState.Idle
    }

}

@Suppress("UNCHECKED_CAST")
class LoginScreenViewModelFactory(private val service: ChImpService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>):  T {
        return LoginScreenViewModel(service.userService) as T
    }
}

