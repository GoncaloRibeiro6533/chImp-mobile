package pt.isel.chimp.channels.channelsList

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.isel.chimp.DependenciesContainer
import pt.isel.chimp.channels.channel.ChannelActivity
import pt.isel.chimp.channels.createChannel.CreateChannelActivity
import pt.isel.chimp.menu.MenuActivity
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
                onMenuRequested = { navigateTo(this, MenuActivity::class.java) },
                onChannelSelected = { channel ->
                    navigateTo(this, ChannelActivity::class.java)
                },
                onNavigateToCreateChannel = { navigateTo(this, CreateChannelActivity::class.java) }
            )
        }

        //TODO maybe disable
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateTo(this@ChannelsListActivity, MenuActivity::class.java)
            }
        })
    }
}

