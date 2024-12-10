package pt.isel.chimp.menu

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.chimp.domain.repository.UserInfoRepository
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.utils.Failure
import pt.isel.chimp.utils.Success

sealed class MenuScreenState {
    object Idle : MenuScreenState()
    object LoggingOut : MenuScreenState()
    object LoggedOut : MenuScreenState()
    data class Error(val error: ApiError) : MenuScreenState()
}

class MenuViewModel(
    private val repo: UserInfoRepository,
    private val service: ChImpService,
    initialState: MenuScreenState = MenuScreenState.Idle
) : ViewModel() {

    var state: MenuScreenState by mutableStateOf(initialState)
        private set

    fun logout() {
        if (state == MenuScreenState.LoggingOut) return
        state = MenuScreenState.LoggingOut
        viewModelScope.launch {
           state =  try {
                repo.clearUserInfo()
                 when (val result = service.userService.logout()) {
                    is Success -> MenuScreenState.LoggedOut
                    is Failure -> MenuScreenState.Error(result.value)
                }
            } catch (e: Exception) {
                MenuScreenState.Error(ApiError("Error logging out"))
            }
        }
    }


}

@Suppress("UNCHECKED_CAST")
class MenuViewModelFactory(
    private val repo: UserInfoRepository,
    private val service: ChImpService
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>):  T {
        return MenuViewModel(
            repo,
            service
        ) as T
    }
}