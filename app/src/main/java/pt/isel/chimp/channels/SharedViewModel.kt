package pt.isel.chimp.channels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.service.ChImpService
import pt.isel.chimp.service.ChannelService

class SharedViewModel (private val channelService: ChannelService)  : ViewModel() {
    var selectedChannel: Channel? = null
}

@Suppress("UNCHECKED_CAST")
class SharedViewModelFactory(private val service: ChImpService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SharedViewModel(service.channelService) as T
    }
}