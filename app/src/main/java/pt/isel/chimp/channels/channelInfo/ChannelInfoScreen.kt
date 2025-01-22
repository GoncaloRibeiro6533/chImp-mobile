package pt.isel.chimp.channels.channelInfo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import pt.isel.chimp.channels.ChannelParcelable
import pt.isel.chimp.channels.UserParcelable
import pt.isel.chimp.components.LoadingView
import pt.isel.chimp.domain.Role
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.profile.ErrorAlert
import pt.isel.chimp.ui.NavigationHandlers
import pt.isel.chimp.ui.TopBar
import pt.isel.chimp.ui.theme.ChImpTheme

@Composable
fun ChannelInfoScreen(
    viewModel: ChannelInfoViewModel,
    channel: Channel,
    role: Role,
    onNavigationBack: (channelP: ChannelParcelable) -> Unit,
    onCreateInvitation: () -> Unit,
    onChannelLeave: () -> Unit
) {
    val state = viewModel.state.collectAsState().value
    ChImpTheme {

        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                TopBar(
                    NavigationHandlers(
                        onBackRequested = {
                            val channelP = if (state is ChannelInfoScreenState.Success) {
                                ChannelParcelable(
                                    id = state.channelInfo.channel.id,
                                    name = state.channelInfo.channel.name,
                                    creator = UserParcelable(state.channelInfo.channel.creator.id,
                                        state.channelInfo.channel.creator.username,
                                        state.channelInfo.channel.creator.email
                                    ),
                                    visibility = state.channelInfo.channel.visibility,
                                    role = role
                                )
                            } else {
                                ChannelParcelable(
                                    id = channel.id,
                                    name = channel.name,
                                    creator = UserParcelable(channel.creator.id,
                                        channel.creator.username,
                                        channel.creator.email
                                    ),
                                    visibility = channel.visibility,
                                    role = role
                                )
                            }
                            onNavigationBack(channelP)
                        }
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
                        viewModel.getChannelMembers(channel)
                    }
                    is ChannelInfoScreenState.Loading -> {
                        LoadingView()
                    }
                    is ChannelInfoScreenState.Success -> {
                        ChannelInfoView(
                            channelInfo = state.channelInfo,
                            onUpdateChannel = { newName ->
                                viewModel.updateChannelName(state.channelInfo, newName) {

                                }
                            },
                            onLeaveChannel = { viewModel.leaveChannel(state.channelInfo.channel){} },
                            onInviteMember = { onCreateInvitation() }
                        )
                    }
                    is ChannelInfoScreenState.Error -> {
                        ErrorAlert(
                            title = "Error",
                            message = state.error.message,
                            buttonText = "Ok",
                            onDismiss = { state.onDismiss() }
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
/*
@Suppress("UNCHECKED_CAST")
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChannelScreenPreview() {
    val preferences: DataStore<Preferences> = preferencesDataStore(name = "preferences") as DataStore<Preferences>
    val cookiesRepo = CookiesRepo(preferences)

    ChannelInfoScreen(
        viewModel = ChannelInfoViewModel(
            UserInfoRepo(preferences),
            ChImpServiceMock(cookiesRepo,
        ),
        Channel(1, "Channel 1 long",
            creator = User(1, "Bob", "bob@example.com"), visibility = Visibility.PUBLIC),
        onNavigationBack = { },
        onChannelLeave = { }
    )
}*/