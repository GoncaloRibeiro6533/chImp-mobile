package pt.isel.chimp.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.http.utils.ApiError
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.UserService


sealed interface HomeScreenState {
    data object Idle : HomeScreenState
    data object Loading : HomeScreenState
    data class Error(val error: ApiError) : HomeScreenState
    data class Success(val channels: List<Channel>) : HomeScreenState
}


class HomeScreenViewModel(private val userService: UserService) : ViewModel() {

    var state: HomeScreenState by mutableStateOf<HomeScreenState>(HomeScreenState.Idle)
        private set

    //TODO check if there is a session and try to . If not, navigate to login screen


}

@Suppress("UNCHECKED_CAST")
class HomeScreenViewModelFactory(private val service: ChImpService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>):  T {
        return HomeScreenViewModel(service.userService) as T
    }
}