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


sealed interface LoginScreenState {
    data object Idle : LoginScreenState
    data object Loading : LoginScreenState
    data class Success(val user: AuthenticatedUser) : LoginScreenState
    data class Error(val exception: Throwable) : LoginScreenState
}

class LoginScreenViewModel(private val services: ChImpService) : ViewModel() {

        var state: LoginScreenState by mutableStateOf<LoginScreenState>(LoginScreenState.Idle)
            private set

        fun login(username: String, password: String) {
            if (state != LoginScreenState.Loading) {
                state = LoginScreenState.Loading
                viewModelScope.launch {
                    state = try {
                        val user = services.userService.login(username, password)
                        LoginScreenState.Success(user)
                    } catch (e: Throwable) {
                        LoginScreenState.Error(e)
                    }
                }
            }
        }

}

@Suppress("UNCHECKED_CAST")
class LoginScreenViewModelFactory(private val service: ChImpService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>):  T {
        return LoginScreenViewModel(service) as T
    }
}

