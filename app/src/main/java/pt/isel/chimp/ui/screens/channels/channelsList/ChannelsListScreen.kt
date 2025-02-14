package pt.isel.chimp.ui.screens.channels.channelsList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.chimp.domain.ChannelParcelable
import pt.isel.chimp.ui.screens.components.LoadingView
import pt.isel.chimp.ui.screens.profile.ErrorAlert
import pt.isel.chimp.ui.NavigationHandlers
import pt.isel.chimp.ui.TopBar
import pt.isel.chimp.ui.theme.ChImpTheme

@Composable
fun ChannelsListScreen(
    viewModel: ChannelsListViewModel,
    onMenuRequested: () -> Unit = { },
    onChannelSelected: (ChannelParcelable) -> Unit = { },
    onNavigateToCreateChannel: () -> Unit = { },
    onFatalError: () -> Unit = { }
) {
    ChImpTheme {
        val state = viewModel.state.collectAsState().value
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopBar(NavigationHandlers(onMenuRequested = onMenuRequested))
            },
            floatingActionButton = { if (state is ChannelsListScreenState.Success) {
                FloatingActionButton(
                    onClick = { onNavigateToCreateChannel() },
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add channel")
                }
            }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            ) {
                Text(
                    text = "Your Channels",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Spacer(modifier = Modifier.padding(8.dp))
                when (state) {
                    is ChannelsListScreenState.Loading -> LoadingView()
                    is ChannelsListScreenState.Uninitialized -> {
                        // do nothing
                    }
                    is ChannelsListScreenState.Success -> {
                        ChannelListView(state.channels) { channel ->
                            onChannelSelected(channel)
                        }
                    }
                    is ChannelsListScreenState.Error -> {
                        ErrorAlert(
                            title = "Error",
                            message = state.error.message,
                            buttonText = "Close app",
                            onDismiss = { onFatalError() }
                        )
                    }
                }
            }
        }
    }
}
