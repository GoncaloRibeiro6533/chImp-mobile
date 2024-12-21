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
import pt.isel.chimp.repository.ChImpRepo
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
    private val userInfo: UserInfoRepository,
    private val service: ChImpService,
    private val repo: ChImpRepo,
    initialState: MenuScreenState = MenuScreenState.Idle
) : ViewModel() {

    var state: MenuScreenState by mutableStateOf(initialState)
        private set

    fun logout() {
        if (state == MenuScreenState.LoggingOut) return
        state = MenuScreenState.LoggingOut
        viewModelScope.launch {
           state =  try {
                 when (val result = service.userService.logout()) {
                    is Success -> {
                        repo.messageRepo.clear()
                        repo.userRepo.clear()
                        repo.channelRepo.clear()
                        userInfo.clearUserInfo()
                        MenuScreenState.LoggedOut
                    }
                    is Failure -> MenuScreenState.Error(result.value)
                }
            } catch (e: Exception) {
               userInfo.clearUserInfo()
               repo.userRepo.clear()
               repo.channelRepo.clear()
               //TODO
               MenuScreenState.Error(ApiError("Error logging out"))
            }
        }
    }


}

@Suppress("UNCHECKED_CAST")
class MenuViewModelFactory(
    private val userInfo: UserInfoRepository,
    private val service: ChImpService,
    private val repo: ChImpRepo
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>):  T {
        return MenuViewModel(
            userInfo,
            service,
            repo
        ) as T
    }
}