package pt.isel.chimp.channels.channelInfo

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import pt.isel.chimp.DependenciesContainer
import pt.isel.chimp.domain.ChannelParcelable
import pt.isel.chimp.channels.channel.ChannelActivity
import pt.isel.chimp.channels.channelsList.ChannelsListActivity
import pt.isel.chimp.channels.createInvitation.CreateInvitationActivity
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.utils.navigateTo

class ChannelInfoActivity: ComponentActivity() {

    private val userInfoRepository by lazy { (application as DependenciesContainer).userInfoRepository }
    private val repo by lazy { (application as DependenciesContainer).repo }
    private lateinit var ch: Channel
    private val viewModel by viewModels< ChannelInfoViewModel>(
        factoryProducer = {
            ChannelInfoViewModelFactory(
                userInfoRepository,
                repo,
                ch
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
        ch = channel.toChannel()
        viewModel.loadChannel(channel.toChannel())
        viewModel.getChannelMembers(channel.toChannel())
        setContent{
            ChannelInfoScreen(
                viewModel = viewModel,
                channel = channel.toChannel(),
                role = channel.role,
                onNavigationBack = { channelP ->
                    val intent = Intent(this, ChannelActivity::class.java).putExtra("channel", channelP)
                    this.startActivity(intent)
                    finish()
                },
                onCreateInvitation = {
                    val intent = Intent(this, CreateInvitationActivity::class.java).putExtra("channel", channel)
                    this.startActivity(intent)
                },
                onChannelLeave = {
                    navigateTo(this, ChannelsListActivity::class.java)
                    finish()
                },
            )
        }
    }
}