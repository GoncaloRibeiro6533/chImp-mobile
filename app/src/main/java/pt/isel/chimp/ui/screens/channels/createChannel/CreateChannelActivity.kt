package pt.isel.chimp.ui.screens.channels.createChannel

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.isel.chimp.DependenciesContainer
import pt.isel.chimp.ui.screens.channels.channel.ChannelActivity
import pt.isel.chimp.domain.ChannelParcelable
import pt.isel.chimp.ui.screens.menu.MenuActivity
import pt.isel.chimp.utils.navigateTo


class CreateChannelActivity : ComponentActivity() {

    private val repo by lazy { (application as DependenciesContainer).repo }
    private val userInfoRepository by lazy { (application as DependenciesContainer).userInfoRepository }

    private val viewModel by viewModels<CreateChannelViewModel>(
        factoryProducer = {
            CreateChannelViewModelFactory(repo, userInfoRepository)
        }
    )

    private fun onNavigationBack() {
        navigateTo(
            this,
            MenuActivity::class.java
        )
        finish()
    }

    private fun navigateToChannel(channel: ChannelParcelable){
        val intent = Intent(this, ChannelActivity::class.java).putExtra("channel", channel)
        this.startActivity(intent)
        finish()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreateChannelScreen(
                viewModel = viewModel,
                onNavigateBack = { onNavigationBack() },
                onChannelCreated = { channel-> navigateToChannel(channel) }
            )
        }
    }
}