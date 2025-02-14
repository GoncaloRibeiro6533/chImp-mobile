package pt.isel.chimp.ui.screens.channels.channel

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import pt.isel.chimp.DependenciesContainer
import pt.isel.chimp.domain.ChannelParcelable
import pt.isel.chimp.ui.screens.channels.channelInfo.ChannelInfoActivity
import pt.isel.chimp.ui.screens.channels.channelsList.ChannelsListActivity
import pt.isel.chimp.utils.navigateTo

class ChannelActivity : ComponentActivity() {

    private val chImpService by lazy { (application as DependenciesContainer).chImpService }
    private val repo by lazy { (application as DependenciesContainer).repo }
    private val viewModel by viewModels<ChannelViewModel>(
        factoryProducer = {
            ChannelViewModelFactory(
                repo,
                chImpService
            )
        }
    )

    private fun navigateToChannelInfo(channel: ChannelParcelable){
        val intent = Intent(this, ChannelInfoActivity::class.java).putExtra("channel", channel)
        this.startActivity(intent)
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?,) {
        super.onCreate(savedInstanceState)
        if(!intent.hasExtra("channel")) {
            navigateTo(this, ChannelsListActivity::class.java)
            finish()
            return
        }
        val channel = intent.getParcelableExtra("channel", ChannelParcelable::class.java)
        if (channel == null) {
            navigateTo(this, ChannelsListActivity::class.java)
            finish()
            return
        }
        viewModel.loadMessages(channel.toChannel())
        setContent{
            ChannelScreen(
                viewModel = viewModel,
                channel =  channel.toChannel(),
                role = channel.role,
                onNavigationBack = {
                    navigateTo(this, ChannelsListActivity::class.java)
                    finish()
                },
                onNavigationChannelInfo = {
                    navigateToChannelInfo(channel)
                }
            )
        }
    }
}