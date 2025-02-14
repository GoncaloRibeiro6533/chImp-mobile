package pt.isel.chimp.ui.screens.channels.searchChannels

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.isel.chimp.DependenciesContainer
import pt.isel.chimp.domain.ChannelParcelable
import pt.isel.chimp.ui.screens.channels.channel.ChannelActivity
import pt.isel.chimp.ui.screens.menu.MenuActivity
import pt.isel.chimp.utils.navigateTo

class SearchChannelsActivity : ComponentActivity() {

    private val userInfoRepository by lazy { (application as DependenciesContainer).userInfoRepository }
    private val repo by lazy { (application as DependenciesContainer).repo }
    private val viewModel by viewModels<SearchChannelsViewModel>(
        factoryProducer = {
            SearchChannelsViewModelFactory(
                userInfoRepository,
                repo
            )
        }
    )

    private fun navigateToChannel(channel: ChannelParcelable){
        val intent = Intent(this, ChannelActivity::class.java)
            .putExtra("channel", channel)
        finish()
        this.startActivity(intent)
    }

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadChannels()
        setContent {
            SearchChannelsScreen(
                viewModel = viewModel,
                onBackRequest = {
                    navigateTo(this@SearchChannelsActivity, MenuActivity::class.java)
                    finish()
                },
                onChannelSelected = { channel ->
                    navigateToChannel(channel)

                },
            )
        }
    }
}