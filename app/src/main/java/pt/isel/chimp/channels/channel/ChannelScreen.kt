package pt.isel.chimp.channels.channel


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import pt.isel.chimp.channels.channelsList.components.ChannelDetailsView
import pt.isel.chimp.components.LoadingView
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.AuthenticatedUser
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.domain.user.UserInChannel
import pt.isel.chimp.infrastructure.UserInfoRepo
import pt.isel.chimp.profile.ErrorAlert
import pt.isel.chimp.service.mock.ChImpServiceMock
import pt.isel.chimp.ui.NavigationHandlers
import pt.isel.chimp.ui.TopBar
import pt.isel.chimp.ui.theme.ChImpTheme

@Composable
fun ChannelScreen(
    viewModel: ChannelViewModel,
    channel: Channel,
    onNavigationBack: () -> Unit,
    onNavigationChannelInfo: () -> Unit
) {
    val user = User (1, "Bob", "bob@email.com")
    val userRole = UserInChannel(user.id, channel.id, Role.READ_WRITE)
    val authenticatedUser = AuthenticatedUser(user, "token1")
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
                when (val state = viewModel.state) {
                    is ChannelScreenState.Initialized -> {
                        viewModel.getMessages(channel.id, 30, 0)
                    }
                    is ChannelScreenState.Idle -> {
                        viewModel.getMessages(channel.id, 30, 0)
                    }
                    is ChannelScreenState.Loading -> {
                        LoadingView()
                    }
                   /* is ChannelScreenState.SuccessOnFindChannel -> {
                        viewModel.getMessages(channel.id, 30, 0)
                    }*/
                    is ChannelScreenState.SuccessOnSendMessage -> {
                        viewModel.getMessages(channel.id, 30, 0)
                    }
                    is ChannelScreenState.Success -> {
                        ChannelView(
                            messages = state.messages,
                            onMessageSend = { content -> viewModel.sendMessage(channel, content) },
                            userRole = userRole
                        )
                    }
                    is ChannelScreenState.Error ->
                        ErrorAlert(
                            title = "Error",
                            message = state.error.message,
                            buttonText = "Ok",
                            onDismiss = { TODO() }
                        )
                }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChannelScreenPreview() {
    val preferences: DataStore<Preferences> = preferencesDataStore(name = "preferences") as DataStore<Preferences>
    ChannelScreen(
        viewModel = ChannelViewModel(
            UserInfoRepo(preferences),
            ChImpServiceMock()
        ),
        channel = Channel(1, "Channel 1 long",
            creator = User(1, "Bob", "bob@example.com"), visibility = Visibility.PUBLIC),
        onNavigationBack = { },
        onNavigationChannelInfo = { },
    )
}

