package pt.isel.chimp.channels.searchChannels

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.isel.chimp.DependenciesContainer
import pt.isel.chimp.channels.ChannelParcelable
import pt.isel.chimp.channels.channel.ChannelActivity
import pt.isel.chimp.menu.MenuActivity
import pt.isel.chimp.utils.navigateTo

class SearchChannelsActivity : ComponentActivity() {

    private val chImpService by lazy { (application as DependenciesContainer).chImpService }
    private val userInfoRepository by lazy { (application as DependenciesContainer).userInfoRepository }
    private val viewModel by viewModels<SearchChannelsViewModel>(
        factoryProducer = {
            SearchChannelsViewModelFactory(
                userInfoRepository,
                chImpService
            )
        }
    )

    private fun navigateToChannel(channel: ChannelParcelable){
        val intent = Intent(this, ChannelActivity::class.java)
                        .putExtra("channel", channel)
        this.startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            val currentUser = userInfoRepository.getUserInfo() ?: error("User not found")
        setContent {
            SearchChannelsScreen(
                viewModel = viewModel,
                currentUser = currentUser,
                onMenuRequested = {
                    navigateTo(this@SearchChannelsActivity, MenuActivity::class.java)
                    finish()
                },
                onChannelSelected = { channel ->
                    navigateToChannel(channel)

                },
            )
        }

            //TODO maybe disable
            onBackPressedDispatcher.addCallback(this@SearchChannelsActivity, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateTo(this@SearchChannelsActivity, MenuActivity::class.java)
                }
            })
        }
    }
}