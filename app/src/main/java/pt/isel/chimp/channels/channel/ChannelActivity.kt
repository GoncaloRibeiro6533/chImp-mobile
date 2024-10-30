package pt.isel.chimp.channels.channel

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.isel.chimp.DependenciesContainer
import pt.isel.chimp.menu.MenuActivity
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
            ChannelScreen(
                viewModel = viewModel,
                onNavigationBack = { finish() },
                onMenuRequested = { navigateTo(this, MenuActivity::class.java) }
            )
        }
    }


}