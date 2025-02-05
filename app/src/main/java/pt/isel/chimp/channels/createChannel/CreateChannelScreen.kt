package pt.isel.chimp.channels.createChannel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.isel.chimp.components.LoadingView
import pt.isel.chimp.domain.ChannelParcelable
import pt.isel.chimp.domain.Role
import pt.isel.chimp.profile.ErrorAlert
import pt.isel.chimp.ui.NavigationHandlers
import pt.isel.chimp.ui.TopBar
import pt.isel.chimp.ui.theme.ChImpTheme

@Composable
fun CreateChannelScreen(
    viewModel: CreateChannelViewModel,
    onNavigateBack: () -> Unit = { },
    onChannelCreated: (ChannelParcelable) -> Unit
) {

    ChImpTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                TopBar(NavigationHandlers(onBackRequested = onNavigateBack))
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (val currentState = viewModel.state.collectAsState().value) {
                    is CreateChannelScreenState.Idle -> {
                        CreateChannelView(onSubmit = { channelName, visibility ->
                            viewModel.createChannel(channelName, visibility)
                        })
                    }
                    is CreateChannelScreenState.Loading -> {
                        LoadingView()
                    }
                    is CreateChannelScreenState.Success -> {
                        onChannelCreated(currentState.channel.toParcelable(Role.READ_WRITE))
                    }
                    is CreateChannelScreenState.Error -> {
                        ErrorAlert(
                            title = "Create Channel Error",
                            message = currentState.error.message,
                            buttonText = "Ok",
                            onDismiss = { viewModel.setIdleState() }
                        )
                    }
                }
            }
        }
    }
}