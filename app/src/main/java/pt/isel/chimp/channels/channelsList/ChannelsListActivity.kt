package pt.isel.chimp.channels.channelsList

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.isel.chimp.DependenciesContainer
import pt.isel.chimp.channels.createChannel.CreateChannelActivity
import pt.isel.chimp.utils.navigateTo

class ChannelsListActivity : ComponentActivity() {

    private val viewModel by viewModels<ChannelsListViewModel>(
        factoryProducer = {
            ChannelsListViewModelFactory((application as DependenciesContainer).chImpService)
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChannelsListScreen(
                viewModel = viewModel,
                onMenuRequested = { /* Handle menu request */ },
                onChannelSelected = { channel -> /* Handle channel selection */ }, //todo navigate to chose channel
                onNavigateToCreateChannel = { navigateTo(this, CreateChannelActivity::class.java) }
            )
        }
    }
}

