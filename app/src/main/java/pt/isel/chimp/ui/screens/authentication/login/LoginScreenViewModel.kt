package pt.isel.chimp.ui.screens.authentication.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.isel.chimp.domain.ApiError
import pt.isel.chimp.domain.repository.UserInfoRepository
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.repository.ChImpRepo
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.UserService
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success


sealed interface LoginScreenState {
    data object Idle : LoginScreenState
    data object Loading : LoginScreenState
    data class Success(val user: User) : LoginScreenState
    data class Error(val error: ApiError) : LoginScreenState
}

class LoginScreenViewModel(
    private val userInfo : UserInfoRepository,
    private val repo: ChImpRepo,
    private val userService: UserService,
    initialState : LoginScreenState = LoginScreenState.Idle
) : ViewModel() {

        private val _state = MutableStateFlow<LoginScreenState>(initialState)
        val state = _state.asStateFlow()

    fun fetchLogin(username: String, password: String) {
            if (_state.value != LoginScreenState.Loading) {
                _state.value = LoginScreenState.Loading
                viewModelScope.launch {
                    _state.value =
                    try {
                        val authUser = userService.login(username, password)
                        when (authUser) {
                            is Success -> {
                                repo.userRepo.insertUser(listOf(authUser.value))
                                userInfo.updateUserInfo(authUser.value)
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
        _state.value = LoginScreenState.Idle
    }

}

@Suppress("UNCHECKED_CAST")
class LoginScreenViewModelFactory(
    private val userInfo: UserInfoRepository,
    private val repo : ChImpRepo,
    private val service: ChImpService
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>):  T {
        return LoginScreenViewModel(
            userInfo,
            repo,
            service.userService
        ) as T
    }
}

