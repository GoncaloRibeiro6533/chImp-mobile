package pt.isel.chimp.channels.createChannel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.isel.chimp.DependenciesContainer


class CreateChannelActivity : ComponentActivity() {
    private val viewModel by viewModels<CreateChannelViewModel>(
        factoryProducer = {
            CreateChannelViewModelFactory((application as DependenciesContainer).chImpService)
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreateChannelScreen(
                //viewModel = viewModel,
                onNavigateBack = { finish()},
                onChannelCreated = { /* Handle channel creation */ }
            )
        }
    }
}