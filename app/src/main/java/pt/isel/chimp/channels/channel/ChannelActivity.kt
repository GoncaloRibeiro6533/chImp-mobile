package pt.isel.chimp.channels.channel

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.isel.chimp.DependenciesContainer
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.menu.MenuActivity
import pt.isel.chimp.utils.navigateTo

class ChannelActivity(private val channel: Channel) : ComponentActivity() {

    private val viewModel by viewModels<ChannelViewModel>(
        factoryProducer = {
            ChannelViewModelFactory((application as DependenciesContainer).chImpService)
        }
    )

    override fun onCreate(savedInstanceState: Bundle?,) {
        super.onCreate(savedInstanceState)
        setContent{
            ChannelScreen(
                viewModel = viewModel,
                //channel = channel,

                channel = Channel(1, "Channel 1",
                    creator = User(1, "Bob", "bob@example.com"), visibility = Visibility.PUBLIC), //TODO: get channel from intent


                onNavigationBack = { finish() },
                onMenuRequested = { navigateTo(this, MenuActivity::class.java) }
            )
        }
    }


}