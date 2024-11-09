package pt.isel.chimp.channels.channel


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.chimp.channels.channelsList.components.ChannelDetailsView
import pt.isel.chimp.components.LoadingView
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.AuthenticatedUser
import pt.isel.chimp.domain.user.User
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
    onNavigationBack: () -> Unit = { },
) {
    val user = User (1, "Bob", "bob@email.com")
    val authenticatedUser = AuthenticatedUser(user, "token1")
    ChImpTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopBar(NavigationHandlers(
                    onBackRequested = onNavigationBack
                ),
                    content = {
                        ChannelDetailsView(channel) //TODO add on channel detail click
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
                    is ChannelScreenState.Success -> {
                        ChannelView(state.messages) { content ->
                            viewModel.sendMessage(channel, authenticatedUser, content, authenticatedUser.token)
                        }
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


@Composable
fun ChatBox(
    modifier: Modifier = Modifier,
    onSendRequest: (String) -> Unit = { }
) {
    var chatBoxValue by remember { mutableStateOf(TextFieldValue("")) }
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = chatBoxValue,
            onValueChange = { newText ->
                chatBoxValue = newText
            },
            placeholder = {
                Text(text = "Type something")
            }
        )
        IconButton(
            onClick = {
            },

            ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send Message")

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
    )
}