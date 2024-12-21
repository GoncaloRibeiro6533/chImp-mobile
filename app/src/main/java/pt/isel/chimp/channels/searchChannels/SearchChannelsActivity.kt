package pt.isel.chimp.channels.searchChannels

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.isel.chimp.DependenciesContainer
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchChannelsScreen(
                viewModel = viewModel,
                onMenuRequested = {
                    navigateTo(this, MenuActivity::class.java)
                    finish()
                },
                onChannelSelected = { channel ->
                    navigateTo(this, ChannelActivity::class.java)
                },
            )

            //TODO maybe disable
            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateTo(this@SearchChannelsActivity, MenuActivity::class.java)
                }
            })
        }
    }
}