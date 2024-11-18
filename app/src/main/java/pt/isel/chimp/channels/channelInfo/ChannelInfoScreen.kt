package pt.isel.chimp.channels.channelInfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.chimp.channels.generalComponents.ChannelLogo
import pt.isel.chimp.components.LoadingView
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.profile.ErrorAlert
import pt.isel.chimp.service.mock.MockChannelService
import pt.isel.chimp.service.repo.RepoMockImpl
import pt.isel.chimp.ui.NavigationHandlers
import pt.isel.chimp.ui.TopBar
import pt.isel.chimp.ui.theme.ChImpTheme

@Composable
fun ChannelInfoScreen(
    viewModel: ChannelInfoViewModel,
    channel: Channel,
    onNavigationBack: () -> Unit,
    onChannelLeave: () -> Unit
) {

    val user = User(1, "Bob", "bob@example.com")

    val state = viewModel.state
    val token = "token1"
    ChImpTheme {

        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                TopBar(
                    NavigationHandlers(
                        onBackRequested = onNavigationBack
                    ),
                    content = {Text(text = "")}
                )
            }
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (state) {
                    is ChannelInfoScreenState.Idle -> {
                        viewModel.getChannelMembers(token, channel)
                    }
                    is ChannelInfoScreenState.Loading -> {
                        LoadingView()
                    }
                    is ChannelInfoScreenState.Success -> {
                        val members = state.channelMembers
                        val num = members.size

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(0.dp)
                                .align(Alignment.CenterHorizontally),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ChannelLogo(channel.name, 135)
                            Text(
                                text = channel.name,
                                modifier = Modifier.padding(10.dp),
                                fontSize = 30.sp
                            )
                            Text(
                                text = "Group - $num members",
                                modifier = Modifier.padding(6.dp)
                            )

                            Row (
                                horizontalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterHorizontally),

                            ) {

                                ChannelDialog(
                                    "Invite Member +",
                                    "Enter user email:",
                                    "Invite", "email",
                                    Color(0xFF32cd32)) { TODO() }

                                ChannelDialog(
                                    "Edit Channel Name",
                                    "Enter new channel name:",
                                    "OK",
                                    channel.name, Color.LightGray) { viewModel.editChannelName() }

                            }

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
                                items(members) { member ->
                                    MemberView(member.first, member.second.role)
                                }
                                item {
                                    Button(
                                        onClick = {
                                            viewModel.leaveChannel(token, channel, user)
                                        },
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(8.dp)
                                            .align(Alignment.CenterHorizontally),
                                        colors = ButtonColors(Color.Red, Color.White, Color.Green, Color.Green)
                                    ) {
                                        Text("Leave Group")

                                    }
                                }
                            }
                        }
                    }
                    is ChannelInfoScreenState.Error -> {
                        ErrorAlert(
                            title = "Error",
                            message = " ",
                            buttonText = "Ok",
                            onDismiss = { viewModel.getChannelMembers(token, channel) }
                        )
                    }

                    is ChannelInfoScreenState.SuccessOnLeave -> {
                        onChannelLeave()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChannelScreenPreview() {
    ChannelInfoScreen(
        viewModel = ChannelInfoViewModel(MockChannelService(RepoMockImpl())),
        Channel(1, "Channel 1 long",
            creator = User(1, "Bob", "bob@example.com"), visibility = Visibility.PUBLIC),
        onNavigationBack = { },
        onChannelLeave = { }
    )
}