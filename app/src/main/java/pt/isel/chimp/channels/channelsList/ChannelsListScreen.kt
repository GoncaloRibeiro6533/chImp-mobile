package pt.isel.chimp.channels.channelsList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import pt.isel.chimp.channels.channelsList.components.ChannelItem
import pt.isel.chimp.components.LoadingView
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.infrastructure.UserInfoRepo
import pt.isel.chimp.profile.ErrorAlert
import pt.isel.chimp.service.mock.MockChannelService
import pt.isel.chimp.service.repo.RepoMockImpl
import pt.isel.chimp.ui.NavigationHandlers
import pt.isel.chimp.ui.TopBar
import pt.isel.chimp.ui.theme.ChImpTheme

@Composable
fun ChannelsListScreen(
    viewModel: ChannelsListViewModel,
    onMenuRequested: () -> Unit = { },
    onChannelSelected: (Channel) -> Unit = { },
    onNavigateToCreateChannel: () -> Unit = { }
) {
    ChImpTheme {
        val state = viewModel.state
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
                when (state) {
                    is ChannelsListScreenState.Initialized ->
                        viewModel.getChannels()

                    is ChannelsListScreenState.Idle -> {
                        viewModel.getChannels()
                    }
                    is ChannelsListScreenState.Loading -> {
                        LoadingView()
                    }
                    is ChannelsListScreenState.Success -> {
                        val channels = state.channels //TODO move to view file
                        Spacer(modifier = Modifier.padding(8.dp))
                        LazyColumn(
                            contentPadding = PaddingValues(
                                top = 0.dp,
                                bottom = innerPadding.calculateBottomPadding(),
                                start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                                end = innerPadding.calculateEndPadding(LayoutDirection.Ltr)
                            ),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(channels) { channel ->
                                ChannelItem(
                                    channel = channel,
                                    onClick = { onChannelSelected(channel) })
                            }
                            if (channels.isEmpty()) {
                                item {
                                    Text(
                                        text = "You don't have any channels yet",
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                        }
                    }
                    is ChannelsListScreenState.Error -> {
                        ErrorAlert(
                            title = "Error",
                            message = state.error.message,
                            buttonText = "Ok",
                            onDismiss = { viewModel.getChannels() }
                        )
                    }
                }
            }
        }

    }
}

@Suppress("UNCHECKED_CAST")
@Preview(showBackground = true , showSystemUi = true)
@Composable
fun PreviewChannelsListScreen() {
    val preferences: DataStore<Preferences> = preferencesDataStore(name = "preferences") as DataStore<Preferences>
    ChannelsListScreen(
        viewModel = ChannelsListViewModel(
            UserInfoRepo(preferences),
            MockChannelService(RepoMockImpl())
        ),
        onMenuRequested = { },
        onChannelSelected = { }
    )
}
