package pt.isel.chimp.channels.channelInfo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import pt.isel.chimp.components.LoadingView
import pt.isel.chimp.domain.channel.Channel
import pt.isel.chimp.domain.channel.Visibility
import pt.isel.chimp.domain.user.User
import pt.isel.chimp.infrastructure.CookiesRepo
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
    val state = viewModel.state.collectAsState().value
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
                        viewModel.getChannelMembers(channel)
                    }
                    is ChannelInfoScreenState.Loading -> {
                        LoadingView()
                    }
                    is ChannelInfoScreenState.Success -> {
                        ChannelInfoView(
                            channelInfo = state.channelInfo,
                            onUpdateChannel = { newName ->
                                viewModel.updateChannelName(state.channelInfo, newName)
                            },
                            onLeaveChannel = { viewModel.leaveChannel(state.channelInfo.channel) }
                        )
                    }
                    is ChannelInfoScreenState.Error -> {
                        ErrorAlert(
                            title = "Error",
                            message = " ",
                            buttonText = "Ok",
                            onDismiss = { viewModel.getChannelMembers(channel) }
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
    val cookiesRepo = CookiesRepo(preferences)
    ChannelInfoScreen(
        viewModel = ChannelInfoViewModel(
            UserInfoRepo(preferences),
            MockChannelService(RepoMockImpl(cookiesRepo), CookiesRepo(preferences))
        ),
        Channel(1, "Channel 1 long",
            creator = User(1, "Bob", "bob@example.com"), visibility = Visibility.PUBLIC),
        onNavigationBack = { },
        onChannelLeave = { }
    )
}