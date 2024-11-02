package pt.isel.chimp.channels.channel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.isel.chimp.DependenciesContainer
import pt.isel.chimp.channels.SharedViewModel
import pt.isel.chimp.channels.SharedViewModelFactory
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User

class ChannelActivity : ComponentActivity() {

    private val viewModel by viewModels<ChannelViewModel>(
        factoryProducer = {
            ChannelViewModelFactory((application as DependenciesContainer).chImpService)
        }
    )

    private val sharedViewModel by viewModels<SharedViewModel>(
        factoryProducer = { SharedViewModelFactory((application as DependenciesContainer).chImpService) }
    )

    override fun onCreate(savedInstanceState: Bundle?,) {
        super.onCreate(savedInstanceState)
        val channel = sharedViewModel.selectedChannel
        setContent{
            ChannelScreen( //todo channel here always null...
                viewModel = viewModel,
                //channel = Channel(1, "Channel 1",
                //    creator = User(1, "Bob", "bob@example.com"), visibility = Visibility.PUBLIC), //TODO: get channel from intent
                channel =  channel ?: Channel(1, "Channel 1", creator = User(1, "Bob", "bob@example.com"), visibility = Visibility.PUBLIC),
                onNavigationBack = { finish() },
                //onMenuRequested = { navigateTo(this, MenuActivity::class.java) }
            )
        }
    }


}