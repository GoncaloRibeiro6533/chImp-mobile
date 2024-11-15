package pt.isel.chimp.channels.channel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.isel.chimp.DependenciesContainer
import pt.isel.chimp.channels.channelInfo.ChannelInfoActivity
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.utils.navigateTo

class ChannelActivity : ComponentActivity() {

    private val viewModel by viewModels<ChannelViewModel>(
        factoryProducer = {
            ChannelViewModelFactory((application as DependenciesContainer).chImpService)
        }
    )


    override fun onCreate(savedInstanceState: Bundle?,) {
        super.onCreate(savedInstanceState)
        setContent{
            ChannelScreen( //todo channel here always null...
                viewModel = viewModel,
               //TODO: get channel from intent
                channel =  Channel(1, "DAW", creator = User(1, "Bob", "bob@example.com"), visibility = Visibility.PUBLIC),
                onNavigationBack = { finish() },
                onNavigationChannelInfo = {
                    navigateTo(this, ChannelInfoActivity::class.java)
                }
            )
        }
    }


}