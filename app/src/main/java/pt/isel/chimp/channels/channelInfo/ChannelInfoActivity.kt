package pt.isel.chimp.channels.channelInfo

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import pt.isel.chimp.DependenciesContainer
import pt.isel.chimp.channels.ChannelParcelable
import pt.isel.chimp.channels.channel.ChannelActivity
import pt.isel.chimp.channels.channelsList.ChannelsListActivity
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.utils.navigateTo

class ChannelInfoActivity: ComponentActivity() {

    private val chImpService by lazy { (application as DependenciesContainer).chImpService }
    private val userInfoRepository by lazy { (application as DependenciesContainer).userInfoRepository }
    private val viewModel by viewModels< ChannelInfoViewModel>(
        factoryProducer = {
            ChannelInfoViewModelFactory(
                userInfoRepository,
                chImpService
            )
        }
    )

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val channel = intent.getParcelableExtra("channel", ChannelParcelable::class.java)
        if (channel == null) {
            navigateTo(this, ChannelsListActivity::class.java)
            finish()
            return
        }
        setContent{
            ChannelInfoScreen(
                viewModel = viewModel,
                channel = channel.toChannel(),
                onNavigationBack = { finish() },
                onChannelLeave = { finish() },
            )
        }
    }
}