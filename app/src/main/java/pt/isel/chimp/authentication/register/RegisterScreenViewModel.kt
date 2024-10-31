package pt.isel.chimp.authentication.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.UserError
import pt.isel.chimp.service.UserService
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success

sealed interface RegisterScreenState {
    data object Idle : RegisterScreenState
    data object Loading : RegisterScreenState
    data class Success(val user: User) : RegisterScreenState
    data class Error(val exception: UserError): RegisterScreenState
}

class RegisterScreenViewModel(private val userServices: UserService) : ViewModel() {

    var state: RegisterScreenState by mutableStateOf(RegisterScreenState.Idle)
        private set

    fun registerUser(username: String, password: String, email: String) {
        if (state != RegisterScreenState.Loading) {
            state = RegisterScreenState.Loading
            viewModelScope.launch {
                val user = userServices.register(username, password, email)
                state = when (user) {
                    is Success -> RegisterScreenState.Success(user.value)
                    is Failure -> RegisterScreenState.Error(user.value)
                }
                /**
                state = try {
                    val user = userServices.register(username, password, email)
                    RegisterScreenState.Success(user)
                } catch (e: Throwable) {
                    RegisterScreenState.Error(e)
                }

                 */
            }
        }
    }

    fun setIdleState() {
        state = RegisterScreenState.Idle
    }
}

@Suppress("UNCHECKED_CAST")
class RegisterScreenViewModelFactory(private val service: ChImpService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegisterScreenViewModel(service.userService) as T
    }
}
