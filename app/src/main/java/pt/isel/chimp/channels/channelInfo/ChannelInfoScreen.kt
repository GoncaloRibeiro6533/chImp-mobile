package pt.isel.chimp.channels.channelInfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import pt.isel.chimp.channels.generalComponents.ChannelLogo
import pt.isel.chimp.components.LoadingView
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.infrastructure.UserInfoRepo
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
                        viewModel.getChannelMembers(channel.id)
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
                                text = "Channel - $num members",
                                modifier = Modifier.padding(6.dp)
                            )

                            Row (
                                horizontalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterHorizontally),

                            ) {

                                ChannelDialog(
                                    "Invite Member +",
                                    "Enter Member Username:",
                                    "Invite",
                                    "username",
                                    Color(0xFF32cd32),
                                    Color.Black) { TODO() }

                                ChannelDialog(
                                    "Edit Channel Name",
                                    "Enter new channel name:",
                                    "OK",
                                    channel.name,
                                    Color.LightGray,
                                    Color.Black) { viewModel.updateChannelName() }

                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            LazyColumn(
                                contentPadding = PaddingValues(
                                    top = 0.dp,
                                    bottom = innerPadding.calculateBottomPadding(),
                                    start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                                    end = innerPadding.calculateEndPadding(LayoutDirection.Ltr)
                                ),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(members) { member ->
                                    MemberView(member.first, member.second, channel.creator)
                                }
                                item {

                                    ChannelDialog(
                                        "Leave Channel",
                                        "Do you want to leave this Channel?",
                                        "Leave", "",
                                        Color.Red,
                                        Color.White) { viewModel.leaveChannel(channel) }


                                    /*
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


                                    }*/
                                }
                            }
                        }
                    }
                    is ChannelInfoScreenState.Error -> {
                        ErrorAlert(
                            title = "Error",
                            message = " ",
                            buttonText = "Ok",
                            onDismiss = { viewModel.getChannelMembers(channel.id) }
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

@Suppress("UNCHECKED_CAST")
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChannelScreenPreview() {
    val preferences: DataStore<Preferences> = preferencesDataStore(name = "preferences") as DataStore<Preferences>
    ChannelInfoScreen(
        viewModel = ChannelInfoViewModel(
            UserInfoRepo(preferences),
            MockChannelService(RepoMockImpl())
        ),
        Channel(1, "Channel 1 long",
            creator = User(1, "Bob", "bob@example.com"), visibility = Visibility.PUBLIC),
        onNavigationBack = { },
        onChannelLeave = { }
    )
}