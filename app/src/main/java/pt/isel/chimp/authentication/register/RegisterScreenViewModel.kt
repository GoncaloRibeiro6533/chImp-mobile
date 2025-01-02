package pt.isel.chimp.authentication.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.chimp.domain.ApiError
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.UserService
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success

sealed interface RegisterScreenState {
    data object Idle : RegisterScreenState
    data object Loading : RegisterScreenState
    data class Success(val user: User) : RegisterScreenState
    data class Error(val error: ApiError): RegisterScreenState
}

class RegisterScreenViewModel(
    private val userServices: UserService,
    initialState: RegisterScreenState = RegisterScreenState.Idle
) : ViewModel() {

    private val _state = MutableStateFlow<RegisterScreenState>(initialState)
    val state = _state.asStateFlow()


    fun registerUser(username: String, password: String, email: String) {
        if (_state.value != RegisterScreenState.Loading) {
            _state.value = RegisterScreenState.Loading
            viewModelScope.launch {
                _state.value = try {
                    val user = userServices.register(username, password, email)
                    when (user) {
                        is Success -> RegisterScreenState.Success(user.value)
                        is Failure -> RegisterScreenState.Error(user.value)
                    }
                } catch (e: Throwable) {
                    RegisterScreenState.Error(ApiError("Error registering user"))
                }
            }
        }
    }

    fun setIdleState() {
        _state.value = RegisterScreenState.Idle
    }
}

@Suppress("UNCHECKED_CAST")
class RegisterScreenViewModelFactory(private val service: ChImpService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegisterScreenViewModel(service.userService) as T
    }
}
