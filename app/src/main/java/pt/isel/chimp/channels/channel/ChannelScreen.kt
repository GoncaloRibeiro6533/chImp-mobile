package pt.isel.chimp.channels.channel


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import pt.isel.chimp.channels.channelsList.components.ChannelDetailsView
import pt.isel.chimp.components.LoadingView
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.profile.ErrorAlert
import pt.isel.chimp.ui.NavigationHandlers
import pt.isel.chimp.ui.TopBar
import pt.isel.chimp.ui.theme.ChImpTheme

@Composable
fun ChannelScreen(
    viewModel: ChannelViewModel,
    channel: Channel,
    role: Role,
    onNavigationBack: () -> Unit,
    onNavigationChannelInfo: () -> Unit
) {
    ChImpTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopBar(NavigationHandlers(
                    onBackRequested = onNavigationBack
                ),
                    content = {
                        Row(modifier = Modifier.clickable(onClick = onNavigationChannelInfo)) {
                            ChannelDetailsView(
                                channel = channel
                            )
                        }
                    }
                )
            },
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                HorizontalDivider()
                when (val state = viewModel.state.collectAsState().value) {
                    is ChannelScreenState.Uninitialized -> {
                        // do nothing
                    }
                    is ChannelScreenState.LoadFromRemote,
                    is ChannelScreenState.SaveData
                        -> {
                        LoadingView()
                        if (state is ChannelScreenState.LoadFromRemote) viewModel.loadRemoteData(channel)
                        if (state is ChannelScreenState.SaveData) viewModel.saveData(
                            state.messages
                        )
                    }

                    is ChannelScreenState.Success -> {
                        ChannelView(
                            messages = state.messages,
                            onMessageSend = { content -> viewModel.sendMessage(channel, content) },
                            userRole = role
                        )
                    }

                    is ChannelScreenState.Error ->
                        ErrorAlert(
                            title = "Error",
                            message = state.error.message,
                            buttonText = "Ok",
                            onDismiss = { onNavigationBack() }
                        )
                }
            }
        }
    }
}