package pt.isel.chimp.channels.channelInfo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.isel.chimp.DependenciesContainer

class ChannelInfoActivity: ComponentActivity() {

    private val viewModel by viewModels<ChannelInfoViewModel>(
        factoryProducer = {
            ChannelInfoViewModelFactory((application as DependenciesContainer).chImpService)
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{

        }

    }
}