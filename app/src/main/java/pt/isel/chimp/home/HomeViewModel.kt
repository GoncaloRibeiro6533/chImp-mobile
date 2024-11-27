package pt.isel.chimp.home

import kotlinx.coroutines.Job
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pt.isel.chimp.domain.repository.UserInfoRepository


sealed interface HomeScreenState {
    data object Idle : HomeScreenState
    data object Logged : HomeScreenState
    data object NotLogged : HomeScreenState
}


class HomeScreenViewModel(
    private val repo: UserInfoRepository,
    initialState : HomeScreenState = HomeScreenState.Idle
) : ViewModel() {

    var state: HomeScreenState by mutableStateOf<HomeScreenState>(initialState)
        private set

    fun getSession(): Job? {
        return viewModelScope.launch {
            delay(700)
            //TODO remove the clear
            repo.clearUserInfo()
            val userInfo = repo.getUserInfo()
            state = if (userInfo != null) {
                HomeScreenState.Logged
            } else {
                HomeScreenState.NotLogged
            }
        }
    }


}

@Suppress("UNCHECKED_CAST")
class HomeScreenViewModelFactory(private val repo: UserInfoRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>):  T {
        return HomeScreenViewModel(repo) as T
    }
}