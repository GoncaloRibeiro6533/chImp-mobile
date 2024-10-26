package pt.isel.chimp.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.service.ChannelError


sealed interface HomeScreenState {
    data object Idle : HomeScreenState
    data object Loading : HomeScreenState
    data class Error(val exception: ChannelError) : HomeScreenState
    data class Success(val channels: List<Channel>) : HomeScreenState
}


class HomeScreenViewModel : ViewModel() {

    var state: HomeScreenState by mutableStateOf<HomeScreenState>(HomeScreenState.Idle)
        private set



}

