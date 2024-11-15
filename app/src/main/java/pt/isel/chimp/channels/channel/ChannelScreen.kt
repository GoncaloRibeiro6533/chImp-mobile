package pt.isel.chimp.channels.channel


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.chimp.channels.channelsList.components.ChannelDetailsView
import pt.isel.chimp.components.LoadingView
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.AuthenticatedUser
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.domain.user.UserInChannel
import pt.isel.chimp.profile.ErrorAlert
import pt.isel.chimp.service.mock.MockChannelService
import pt.isel.chimp.service.mock.MockMessageService
import pt.isel.chimp.service.repo.RepoMockImpl
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
                        ChannelDetailsView(
                        channel = channel,
                        onClick = onNavigationChannelInfo,
                        enabled = true
                        )
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
                    is ChannelScreenState.Idle -> {
                        //viewModel.findChannelById(channel.id, authenticatedUser.token) //TODO why this calling?
                        viewModel.getMessages(channel.id, 10, 0, authenticatedUser.token)
                    }
                    is ChannelScreenState.Loading -> {
                        LoadingView()
                    }
                    is ChannelScreenState.SuccessOnFindChannel -> {
                        viewModel.getMessages(channel.id, 30, 0, authenticatedUser.token)
                    }
                    is ChannelScreenState.SuccessOnSendMessage -> {
                        viewModel.getMessages(channel.id, 30, 0, authenticatedUser.token)
                    }
                    is ChannelScreenState.Success -> { //TODO
                        ChannelView(
                            messages = state.messages,
                            onMessageSend = { content -> viewModel.sendMessage(channel, authenticatedUser, content, authenticatedUser.token) },
                            userRole = userRole
                        )
                    }
                    is ChannelScreenState.Error ->
                        ErrorAlert(
                            title = "Error",
                            message = state.error.message,
                            buttonText = "Ok",
                            onDismiss = { viewModel.findChannelById(channel.id, authenticatedUser.token) }
                        )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChannelScreenPreview() {
    ChannelScreen(
        viewModel = ChannelViewModel(MockChannelService(RepoMockImpl()), MockMessageService(RepoMockImpl())),
        channel = Channel(1, "Channel 1 long",
            creator = User(1, "Bob", "bob@example.com"), visibility = Visibility.PUBLIC),
        onNavigationBack = { },
        onNavigationChannelInfo = { },
    )
}

