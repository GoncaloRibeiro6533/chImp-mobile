package pt.isel.chimp.channels.createChannel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.isel.chimp.DependenciesContainer
import pt.isel.chimp.channels.channelsList.ChannelsListActivity
import pt.isel.chimp.menu.MenuActivity
import pt.isel.chimp.utils.navigateTo


class CreateChannelActivity : ComponentActivity() {

    private val chImpService by lazy { (application as DependenciesContainer).chImpService }
    private val repo by lazy { (application as DependenciesContainer).repo }
    private val userInfoRepository by lazy { (application as DependenciesContainer).userInfoRepository }

    private val viewModel by viewModels<CreateChannelViewModel>(
        factoryProducer = {
            CreateChannelViewModelFactory(chImpService, repo, userInfoRepository)
        }
    )

    private fun onNavigationBack() {
        navigateTo(
            this,
            MenuActivity::class.java
        )
        finish()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreateChannelScreen(
                viewModel = viewModel,
                onNavigateBack = { onNavigationBack() },
                onChannelCreated = { navigateTo(this, ChannelsListActivity::class.java) } //todo move to all channels or directly to channel?
            )
        }
    }
}