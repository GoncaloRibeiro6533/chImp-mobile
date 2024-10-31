package pt.isel.chimp.channels.channel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.chimp.channels.generalComponents.ChannelLogo
import pt.isel.chimp.components.LoadingView
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.message.MessageView
import pt.isel.chimp.service.MockChannelService
import pt.isel.chimp.service.MockMessageService
import pt.isel.chimp.service.repo.RepoMockImpl
import pt.isel.chimp.ui.NavigationHandlers
import pt.isel.chimp.ui.TopBar
import pt.isel.chimp.ui.theme.ChImpTheme

@Composable
fun ChannelScreen(
    viewModel: ChannelViewModel,
    channel: Channel,
    onNavigationBack: () -> Unit = { },
    onMenuRequested : () -> Unit = { },
) {

    val state = viewModel.state

    ChImpTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopBar(NavigationHandlers(
                    onBackRequested = onNavigationBack,
                    onMenuRequested = onMenuRequested))
            },
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ChannelLogo(initial = channel.name.first())
                    Text(
                        text = channel.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                when (state) {
                    is ChannelScreenState.Idle -> {
                        //viewModel.findChannelById(channel.id, channel.creator)
                        viewModel.getMessages(channel.id, 10, 10)
                    }
                    is ChannelScreenState.Loading -> {
                        LoadingView()
                    }
                    is ChannelScreenState.Success -> {

                        val messages = state.messages
                        Box(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()

                        ) {
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
                                items(messages) { message ->
                                    MessageView(
                                        user = message.sender,
                                        message = message.content,
                                    )
                                }

                            }
                            var chatBoxValue by remember { mutableStateOf(TextFieldValue("")) }
                            Box (
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier
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
                                            viewModel.sendMessage(channel.creator, channel, chatBoxValue.text)
                                        },

                                        ) {
                                        Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send Message")

                                    }
                                }
                            }
                        }
                    }

                    is ChannelScreenState.MsgError -> {
                        Text(
                            text = state.exception.message,
                            modifier = Modifier.padding(innerPadding))
                    }

                    is ChannelScreenState.ChError -> {
                        Text(
                            text = state.exception.message,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }


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
        channel = Channel(1, "Channel 1",
            creator = User(1, "Bob", "bob@example.com"), visibility = Visibility.PUBLIC),
        onNavigationBack = { },
        onMenuRequested = { }
    )
}