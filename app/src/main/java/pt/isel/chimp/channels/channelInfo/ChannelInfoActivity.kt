package pt.isel.chimp.channels.channelInfo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.isel.chimp.DependenciesContainer
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User

class ChannelInfoActivity: ComponentActivity() {

    private val viewModel by viewModels<ChannelInfoViewModel>(
        factoryProducer = {
            ChannelInfoViewModelFactory((application as DependenciesContainer).chImpService)
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            ChannelInfoScreen(
                viewModel = viewModel,
                channel = Channel(1, "DAW", creator = User(1, "Bob", "bob@example.com"), visibility = Visibility.PUBLIC),
                onNavigationBack = { finish() },
                onChannelLeave = { finish() },
            )

        }
    }
}