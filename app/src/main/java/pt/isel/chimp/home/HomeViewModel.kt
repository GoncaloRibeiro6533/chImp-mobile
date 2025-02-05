package pt.isel.chimp.home

import kotlinx.coroutines.Job
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Url
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pt.isel.chimp.ChImpApplication
import pt.isel.chimp.domain.repository.UserInfoRepository


sealed interface HomeScreenState {
    data object Idle : HomeScreenState
    data object Logged : HomeScreenState
    data object NotLogged : HomeScreenState
}


class HomeScreenViewModel(
    private val repo: UserInfoRepository,
    private val cookiesStorage: CookiesStorage,
    initialState : HomeScreenState = HomeScreenState.Idle
) : ViewModel() {

    var state: HomeScreenState by mutableStateOf<HomeScreenState>(initialState)
        private set

    fun getSession(): Job? {
        return viewModelScope.launch {
            try {
                val userInfo = repo.getUserInfo()
                state = if (userInfo != null) {
                    HomeScreenState.Logged
                } else {
                    HomeScreenState.NotLogged
                }
            } catch (e: Exception) {
                state = HomeScreenState.NotLogged
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repo.clearUserInfo()
            state = HomeScreenState.NotLogged
        }
    }


}

@Suppress("UNCHECKED_CAST")
class HomeScreenViewModelFactory(
    private val repo: UserInfoRepository,
    private val cookiesStorage: CookiesStorage
    ): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>):  T {
        return HomeScreenViewModel(repo, cookiesStorage) as T
    }
}